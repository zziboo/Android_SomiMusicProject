package com.example.zziboo.somimusic;

import android.app.Activity;
import android.content.Entity;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MusicActivity extends AppCompatActivity {
    ListView musicList;
    Button playButton;
    ArrayAdapter adapter;
    String titleName, musicPath;
    MediaPlayer mp;
    TextView playMusictxt;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0: //list
                    String[] titleList;
                    titleList = ((String) msg.obj).split("%");
                    for (int i = 0; i < titleList.length; ++i) {
                        titleList[i] = titleList[i];
                    }
                    adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, titleList);
                    musicList.setAdapter(adapter);
                    musicList.setBackgroundColor(Color.argb(255, 222, 223, 226));


                    break;
                case 1: //file
                    musicPath = ((File) msg.obj).getAbsolutePath();
                    try {
                        if (mp != null) {
                            mp.release();
                            mp=null;
                        }
                        mp=new MediaPlayer();
                        mp.setDataSource(musicPath);
                        mp.prepare();
                        mp.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        musicList = (ListView) findViewById(R.id.musicList);
        playButton = (Button) findViewById(R.id.playbtn);
        playMusictxt = (TextView) findViewById(R.id.playMusictxt);

        mp = null;

        new HttpService("list", handler, getApplicationContext()).start(); //HttpService(String command, Handler handler, Context context)
        musicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                titleName = musicList.getItemAtPosition(position).toString();
                playMusictxt.setText(titleName);
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new HttpService("title=" + URLEncoder.encode(titleName, "utf-8"), handler, getApplicationContext()).start();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
