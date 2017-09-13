package com.example.changho.kidmanagement.service;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.example.changho.kidmanagement.utils.SharedData;
import com.example.changho.kidmanagement.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Changho on 2016-10-31.
 */

public class ManagementServiceImpl implements ManagementService {
    Utils utils = new Utils();
    Context context;

    public ManagementServiceImpl(Context context) {
        this.context = context;
    }

    // get app info
    @Override
    public void getAppInfo() {
        new GetClientAppInfo(context).execute();
    }

    @Override
    public void synkChildAppState() {
//        for(HashMap<String,String> hashMap : SharedData.appList) {
            //String packageName = hashMap.get("packageName");
        for(int i = 0; i< SharedData.appList.size(); i++) {
//            this.packageName = "com.kakao.talk";
            String packageName = SharedData.appList.get(i).get("packageName");
            Log.d("packname...", packageName);
            new PullAppInfoAsyncTask().execute("http://124.5.146.72:8088/upload/" + packageName + ".jpg", packageName);
        }
//        }
    }

    // network
    private class PullAppInfoAsyncTask extends AsyncTask<String, Void, Void> {
        private final static String SAVE_DIR = "/appicon";

        @Override
        protected Void doInBackground(String... strings) {

            //save path
            String savePath = Environment.getExternalStorageDirectory().toString() + SAVE_DIR;
            utils.printLog(savePath);

            File dir = new File(savePath);

            //상위 디렉토리가 존재하지 않을 경우 생성
            if (!dir.exists()) {
                dir.mkdirs();
            }


            //웹 서버 쪽 파일이 있는 경로
            String fileUrl = strings[0];

            String pName = strings[1];
            //다운로드 폴더에 동일한 파일명이 존재하는지 확인
//                if (new File(savePath + "/" + fileName).exists() == false) {
//                } else {
//                }

            //String localPath = savePath + "/" + fileName + ".jpg";
            String localPath = savePath + "/" + pName + ".jpg";

            try {
                URL imgUrl = new URL(fileUrl);
                //서버와 접속하는 클라이언트 객체 생성
                HttpURLConnection conn = (HttpURLConnection) imgUrl.openConnection();
                int len = conn.getContentLength();
                byte[] tmpByte = new byte[len];

                //입력 스트림을 구한다
                InputStream is = conn.getInputStream();
                File file = new File(localPath);

                //파일 저장 스트림 생성
                FileOutputStream fos = new FileOutputStream(file);
                int read;
                //입력 스트림을 파일로 저장
                for (; ; ) {
                    read = is.read(tmpByte);
                    if (read <= 0) {
                        break;
                    }
                    fos.write(tmpByte, 0, read); //file 생성
                }
                is.close();
                fos.close();
                conn.disconnect();
            }catch(Exception e){e.printStackTrace();}

            return null;
        }
    }
}
