package com.example.zziboo.somimusic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button  musicButton, closeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 메인 화면에 있는 버튼 선언
        musicButton = (Button) findViewById(R.id.musicbtn);
        closeButton = (Button) findViewById(R.id.closebtn);

        // close button 클릭 리스너
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "어플리케이션을 종료합니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // music button 리스너, music list를 출력하는 화면으로 전환
        musicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MusicActivity.class);
                startActivity(intent);
            }
        });
    }
}
