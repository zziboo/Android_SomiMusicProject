package com.example.zziboo.somimusic;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by zziboo on 2017-06-15.
 */

public class HttpService extends Thread {

    String urlcommand;
    Handler handler;
    int commandflag; // 0=list, 1=file
    Message msg;
    Object msgobj;
    Context context;

    public HttpService(String command, Handler handler, Context context){
        urlcommand = command;
        this.handler = handler;
        msgobj = null;
        this.context = context;
    }

    @Override
    public void run() {

        // HTTP 서비스 호출 (서버 IP : 210.125.31.119:8085)
        String urlstr = "http://210.125.31.119:8080/ads/android"+ "?" + urlcommand;

        try {
            URL url = new URL(urlstr);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            StringBuilder responseStringBuilder = new StringBuilder();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                if(urlcommand.equalsIgnoreCase("list")){
                    BufferedReader bufferedReader;

                    bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    for (;;){
                        String stringLine = bufferedReader.readLine();
                        if (stringLine == null ) break;
                        responseStringBuilder.append(stringLine); // + "\n"
                    }
                    msgobj = responseStringBuilder.toString();
                    commandflag = 0;
                    bufferedReader.close();
                }else{
                    byte[] bytearr = new byte[4096];
                    String musicPath = context.getFilesDir().getPath() + "/";
                    String titlename = urlcommand.split("=")[1];
                    File file = new File(musicPath + titlename);
                    OutputStream opstream = new FileOutputStream(file);
                    InputStream ipstream = connection.getInputStream();
                    int buffsize;
                    while((buffsize=ipstream.read(bytearr)) != -1){
                        opstream.write(bytearr, 0, buffsize);
                    }
                    msgobj = file;
                    commandflag = 1;
                }
            }

            connection.disconnect();

            Log.d("buffer", responseStringBuilder.toString());

            msg = handler.obtainMessage();
            msg.what = commandflag;
            msg.obj = msgobj;
            handler.sendMessage(msg);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
