package com.example.zziboo.somimusic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

public class MusicActivity extends AppCompatActivity {
    ListView musicList;
    Button playButton, downloadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        musicList = (ListView) findViewById(R.id.musicList);

        playButton = (Button) findViewById(R.id.playbtn);
        downloadButton = (Button) findViewById(R.id.downloadbtn);
    }
}
