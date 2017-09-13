package com.example.changho.changholock;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Changho on 2016-10-03.
 */
public class NetworkAccess {
    final String url = "http://117.17.142.141:7070/capstonserver/android";  //

    public NetworkAccess() {
        Log.i("networkAccess called","networkAccess called");
        AsyncTaskTest asyn =  new AsyncTaskTest();
        asyn.execute();
    }

    //AsyncTask는 inner class로
   private class AsyncTaskTest extends AsyncTask<Void , String , String> {
        int HttpResult;
        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            Log.i("preExecute 호출","call");
        }
        @Override
        protected String doInBackground(Void... voids) {
            Log.i("doInBackground call","call");
            BufferedReader br;
            try {
                URL object = new URL(url);
                HttpURLConnection con = (HttpURLConnection) object.openConnection();

                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestMethod("POST"); // web 서버 requestmapping 설정 필요 ?

                OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                //
                JSONObject parent = new JSONObject();


                //app lists

                //
//                parent.put("name", "Amanda8");
//                //
//                parent.put("id", "2238");
//                parent.put("password", "1234");
                //testing
                int size = 0;
                for(int i=0; i<SharedData.installedApps.size(); i++){
                    String data = Integer.toString(i);
                    parent.put(data,SharedData.installedApps.get(i));
                }

                wr.write(parent.toString());
                wr.flush();

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
                    Log.i("receivedJsonData: ",receivedJsonDataString);
                    //

                    //
                    return receivedJsonDataString;
                } else {
                    //System.out.println(con.getResponseMessage());
                    Log.i("HTTP fail", "" + con.getResponseMessage());
                    return null;
                }
            }catch(Exception e){
                e.printStackTrace();
                Log.i("e.getMessage",e.getMessage());
                Log.i("e.toString",e.toString());
                return null;
            }
        }
        @Override
        protected void onPostExecute(String result) {
            //super.onPostExecute(result);
            Log.i("postExecute call","call");
            Log.i("result: ",result);
            try {
                JSONObject obj = new JSONObject(result);
                String name = (String) obj.get("name");
                String password = (String) obj.get("password");

                Log.i("name ",name);
                Log.i("password ",password);
            }catch (Exception e){}
        }
    }
}
