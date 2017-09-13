package com.example.changho.kidmanagement.service;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.changho.kidmanagement.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Changho on 2016-11-01.
 */

public class SendTargets extends AsyncTask<Void, Void, Void> {
    String url = "http://124.5.146.72:8088/capstonserver/fcm/send";
    int HttpResult;
    BufferedReader br;
    Context context;
    String time;
    public SendTargets(Context context,String time) {
        this.context = context; this.time = time;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL sendTargetUrl = new URL(url);
            //서버와 접속하는 클라이언트 객체 생성
            HttpURLConnection connection = (HttpURLConnection) sendTargetUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","application/json");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            JSONObject json = new JSONObject();
            JSONArray jsonArray = new JSONArray();

            ArrayList<String> arrayList = Utils.getStringArrayPref(context,"targetApps");
            for(String str : arrayList){
                Log.d("app ",str);
                jsonArray.put(str);
            }
            json.put("targets",jsonArray);
            //
            json.put("time",time);
            //
            Log.d("json string ",json.toString());
            OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
            wr.write(json.toString());
            wr.flush();

            int responseCode = connection.getResponseCode();
            if(responseCode == 200){
                Log.d("httpConnection sucess","sucess");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
