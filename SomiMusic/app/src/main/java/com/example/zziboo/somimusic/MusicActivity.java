package com.example.zziboo.somimusic;

import android.app.Activity;
import android.content.Entity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.List;

public class MusicActivity extends AppCompatActivity {
    ListView musicList;
    Button playButton, downloadButton;
    CustomList adapter;

    private final Handler httphandle = new Handler(){
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            String rtnMsg;
            switch(msg.what){
                case 0:
                    rtnMsg = (String) msg.obj;
                    Toast.makeText(getApplicationContext(), rtnMsg + " Service Fail", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    rtnMsg = (String) msg.obj;
                    Toast.makeText(getApplicationContext(), rtnMsg + " Service Start", Toast.LENGTH_SHORT).show();
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
        downloadButton = (Button) findViewById(R.id.downloadbtn);

        adapter = new CustomList(MusicActivity.this);

        // HTTP 서비스 호출 (서버 IP : 210.125.31.119:8085)
        String url = "210.125.31.119:8085/ads/android";
        int param = 1;
        String message = "what????"; // 대규모 문자열 메시지
        new CallHttp(url, param, message, httphandle).start();
    }

    //HTTP 호출 스레드
    private class CallHttp extends Thread{
        private String url, msg;
        private int para;
        private Handler hand;

        public CallHttp(String url, int para, String msg, Handler hand){
            this.url = url;
            this.para = para;
            this.msg = msg;
            this.hand = hand;
        }

        public void run(){
            Message message = hand.obtainMessage();
            try{
                HttpClient httpCilent = new DefaultHttpClient();
                HttpParams httpParams = httpCilent.getParams();
                HttpPost httpPost = new HttpPost("http://" + url + "?param=" + para);
                List<NameValuePair> paramList = new ArrayList<NameValuePair>();
                paramList.add(new BasicNameValuePair("params", msg));
                httpPost.setEntity(new UrlEncodedFormEntity(paramList, "utf-8"));
                httpPost.addHeader("text", "plain");

                HttpResponse response = httpCilent.execute(httpPost);
                String str = EntityUtils.toString(response.getEntity(), "utf-8");

                message.what = 1;
                message.arg1 = para;
                message.obj = str;
                hand.sendMessage(message);
            }catch(Exception e){
                message.what = 0;
                message.arg1 = para;
                hand.sendMessage(message);
            }
        }
    }


    // Custom List
    public class CustomList extends BaseAdapter {
        private final Activity context;
        public CustomList(Activity context){
            this.context = context;
        }

        @Override
        public int getCount() {
            return 0;
            //return msgDatasetVector.size();
        }

        @Override
        public Object getItem(int position) {
            return 0;
            //return msgDatasetVector.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent){ //肄쒕갚硫붿냼??
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.listitem, null, true);
            ImageView imageItem = (ImageView) rowView.findViewById(R.id.imageItem);
            TextView titleItem = (TextView) rowView.findViewById(R.id.titleItem);
            TextView singerItem = (TextView) rowView.findViewById(R.id.singerItem);
            //imageItem.setImage
            //titleItem.setText(msgDatasetVector.get(position).getName());
            //singerItem.setText(msgDatasetVector.get(position).getPhoneNumber());
            return rowView;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
