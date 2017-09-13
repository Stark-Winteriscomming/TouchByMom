package com.example.changho.kidmanagement.service;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.changho.kidmanagement.utils.SharedData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by Changho on 2016-11-01.
 */

public class GetClientAppInfo extends AsyncTask<Void, Void, Void>{

    String url = "http://124.5.146.72:8088/open";
    int HttpResult;
    BufferedReader br;
    Context context;

    public GetClientAppInfo(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL appInfoUrl = new URL(url);
            //서버와 접속하는 클라이언트 객체 생성
            HttpURLConnection con = (HttpURLConnection) appInfoUrl.openConnection();

            con.setDoOutput(true);
            con.setDoInput(true);
            //con.setRequestProperty("Content-Type", "application/json");
            //con.setRequestProperty("Accept", "application/json");
            con.setRequestMethod("POST"); // web 서버 requestmapping 설정 필요 ?

            HttpResult = con.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                br = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), "utf-8"));
                String line = null;
                String receivedJsonDataString = "";
                while ((line = br.readLine()) != null) {
                    receivedJsonDataString += (line + "\n");
                }
                br.close();
                Log.d("receivedJsonData: ", receivedJsonDataString);
                /////////////////////
                JSONObject json = new JSONObject(receivedJsonDataString);
                JSONArray jsonArray = new JSONArray(json.getString("appList"));
//                String packageName = (String) jsonArray.getJSONObject(0).get("packageName");
//                String appName = (String) jsonArray.getJSONObject(0).get("appName");

                // sharedPreferce update
//                Log.d("app name ",appName);
//                Log.d("package name",packageName);
                for (int i=0; i<jsonArray.length(); i++){
                    String packageName = (String) jsonArray.getJSONObject(i).get("packageName");
                    String appName = (String) jsonArray.getJSONObject(i).get("appName");
                    HashMap<String,String> hashMap = new HashMap<String,String>();
                    hashMap.put("packageName",packageName);
                    hashMap.put("appName",appName);
                    SharedData.appList.add(hashMap);
                }
            } else {
                Log.i("HTTP fail", "" + con.getResponseMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        DbService dbService = new DbService(context);;
        dbService.insertApps(SharedData.appList);
    }
}
