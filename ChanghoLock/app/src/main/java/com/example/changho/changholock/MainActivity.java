package com.example.changho.changholock;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Target;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public static boolean mStop;
    public static boolean mPassApp=false;
    private Button btn_sendAppList;
    private Button btn_sendImg;
    private PackageManager pm;
    private List<ResolveInfo> installedAppList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(FirebaseInstanceId.getInstance().getToken() != null)
        Log.d("token: ",FirebaseInstanceId.getInstance().getToken());
        /**
         * 접근성 허용
         */
        ImageView img_access = (ImageView)findViewById(R.id.IMG_ACCESS);
        img_access.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivityForResult(intent, 0);
            }
        });
        pm = this.getApplicationContext().getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        installedAppList = pm.queryIntentActivities(intent, 0);

        for (ResolveInfo applicationInfo : installedAppList) {
            String pName = applicationInfo.activityInfo.packageName;   // 앱 패키지 이름

            // 공유데이터 영역에 설치된 앱 정보를 리스트로 저장
            SharedData.installedApps.add(pName);
        }

        // 처음 액티비티 생성했을 때 자동포함된 코드
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        btn_sendImg = (Button)findViewById(R.id.sendImg);
        btn_sendAppList = (Button)findViewById(R.id.sendAppList);
        //
//        btn_sendImg.setVisibility(View.INVISIBLE);
//        btn_sendAppList.setVisibility(View.INVISIBLE);
        //
        final PackageManager pm = getApplicationContext().getPackageManager();
        Intent intent1 = new Intent(Intent.ACTION_MAIN);
        intent1.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> list = pm.queryIntentActivities(intent1, 0);

        //
//        for (ResolveInfo applicationInfo : list) {
//            Drawable iconDrawable = applicationInfo.loadIcon(pm);   // 앱 아이콘
//            Bitmap bitmap = Utils.drawableToBitmap(iconDrawable);
//            break;
//        }
        /**
         * 설치된 앱 목록 보내기
         */
        btn_sendAppList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // TODO: 2016-10-24
                Log.i("log check ","check");
                sendInstalledAppList();

            }
        });

        // 앱 아이콘 보내기
        btn_sendImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             //sendImage();

                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        final PackageManager pm = getApplicationContext().getPackageManager();
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_LAUNCHER);
                        List<ResolveInfo> list = pm.queryIntentActivities(intent, 0);

                        //
                        for (ResolveInfo applicationInfo : list) {
                            Drawable iconDrawable = applicationInfo.loadIcon(pm);   // 앱 아이콘
                            String packageName  = applicationInfo.activityInfo.packageName; // 패키지 이름
                            Bitmap bitmap = Utils.drawableToBitmap(iconDrawable);
//                            imageView.setImageBitmap(bitmap);
                            sendBitMap(bitmap , packageName);
                        }
                    }
                };

                thread.start();

                //
            }
        });

        /**
         * 디바이스 매니저 관리
         */
        final Intent DMIntent = new Intent(this, Controller.class);
        ImageView img_device_manager = (ImageView)findViewById(R.id.IMG_DEVICE_MANAGER);
        img_device_manager.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(DMIntent);
            }
        });

        /**
         * 잠긴 앱 보기
         */
        final Intent showAppIntent = new Intent(MainActivity.this, TargetedApp.class);
        ImageView img_seeingApp = (ImageView)findViewById(R.id.IMG_SEEING_APP);
        img_seeingApp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.i("seeing app","clicked");
                startActivity(showAppIntent);
            }
        });
    //
    }// end of onCreate

    // 1020
    private String setImage(String imgName , String img){
        return "Content-Disposition: form-data; name=\""+imgName+"\";filename=\""+img+"\"\r\n";
    }


    // 서버로 비트맵 이미지 전송
// 이미지
    private void sendBitMap(Bitmap bitmap , String fileName) {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
        byte [] ba = bao.toByteArray();
//        final String ba1 = Base64.encodeToString(ba, Base64.DEFAULT);
        Log.d("bitmap height ",""+bitmap.getHeight());
        // 기타 필요한 내용
//        String attachmentName = "bitmap";
//        String attachmentFileName = "bitmap.jpg";
        String attachmentName = fileName;
        String attachmentFileName  = fileName + ".jpg";
        String crlf = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        // request 준비
        HttpURLConnection httpUrlConnection = null;
        try {
            URL url = new URL("http://124.5.146.72:8088/capstonserver/sendbitmap");
            httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setUseCaches(false);
            httpUrlConnection.setDoOutput(true);


            httpUrlConnection.setRequestMethod("POST");
            httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
            httpUrlConnection.setRequestProperty("Cache-Control", "no-cache");
            httpUrlConnection.setRequestProperty(
                    "Content-Type", "multipart/form-data;boundary=" + boundary);

            // content wrapper시작
            DataOutputStream request = new DataOutputStream(
                    httpUrlConnection.getOutputStream());

            request.writeBytes(twoHyphens + boundary + crlf);
            request.writeBytes("Content-Disposition: form-data; name=\"" +
                    attachmentName + "\";filename=\"" +
                    attachmentFileName + "\"" + crlf);
            request.writeBytes(crlf);

            request.write(ba);

// content wrapper종료
            request.writeBytes(crlf);
            request.writeBytes(twoHyphens + boundary +
                    twoHyphens + crlf);

// buffer flush
            request.flush();
            request.close();

            // Response받기
//            InputStream responseStream = new
//                    BufferedInputStream(httpUrlConnection.getInputStream());
//            BufferedReader responseStreamReader =
//                    new BufferedReader(new InputStreamReader(responseStream));
//            String line = "";
//            StringBuilder stringBuilder = new StringBuilder();
//            while ((line = responseStreamReader.readLine()) != null) {
//                stringBuilder.append(line).append("\n");
//            }
//            responseStreamReader.close();
//            String response = stringBuilder.toString();

            // 응답 코드 받기
            int code = httpUrlConnection.getResponseCode();
            Log.d("response code ",""+code);
//            Log.d("response code ",response);

//Response stream종료
//            responseStream.close();

// connection종료
            httpUrlConnection.disconnect();
        }catch (Exception e){e.printStackTrace();}
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // TODO: 2016-10-24
    /**
     * 설치된 앱 목록 보내기
     */
    public void sendInstalledAppList(){
        Thread sendInstalledAppListThread = new Thread(){
            @Override
            public void run() {
                try {

                    HttpURLConnection connection = new HttpConnection("http://124.5.146.72:8088/capstonserver/android/sendinstalledapplist").getConn();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type","application/json");
                    connection.setUseCaches(false);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);

                    JSONObject json = new JSONObject();
                    JSONArray jsonArray = new JSONArray();

                    for (ResolveInfo applicationInfo : installedAppList) {
                        String pName = applicationInfo.activityInfo.packageName;   // 앱 패키지 이름
                        String name = String.valueOf(applicationInfo.loadLabel(pm));    // 앱 이름


                        JSONObject app = new JSONObject();
                        app.put("appName",name);
                        app.put("packageName",pName);
                        jsonArray.put(app);


                    }
                    json.put("appList",jsonArray);

                    OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
                    wr.write(json.toString());
                    wr.flush();

                    int responseCode = connection.getResponseCode();
                    if(responseCode == 200){
                        Log.d("httpConnection sucess","sucess");
                    }


                }catch(Exception e){e.printStackTrace();}
            }
        };
        sendInstalledAppListThread.start();
    }
}
