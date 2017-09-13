package com.example.changho.changholock;

/**
 * Created by changho on 2016-03-27.
 */
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;

public class TimerService extends Service {
    //variables for timer
    //private Handler mHandler;
    private Runnable mRunnable;
    int sum_time = 0;
    int over_time;

    //현재 시점의 sharedData 기록
//    private ArrayList<String> targets = SharedPreference.getStringArrayPref(this,"urls");

    public TimerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "Service start", Toast.LENGTH_LONG).show();
        //


//
//        mHandler = new Handler();
//        mHandler.postDelayed(mRunnable, 10000);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
//        mHandler.removeCallbacks(mRunnable);
        super.onDestroy();
    }

    //me: onStratCommand에 주 수행 작업을 코딩하는 듯
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final ArrayList<String> targets = SharedPreference.getStringArrayPref(this,"urls");
        String str_overTime = intent.getStringExtra("time");
        Log.d("time",str_overTime);
        for(String target : targets){
            Log.d("target ",target);
        }
        Log.d("hashmap size ",SharedData.hashMapArrayList.size()+"");
        over_time = Integer.parseInt(str_overTime);
        mRunnable = new Runnable() {
            // 시간초과 확인 flag
            Boolean timeOverFlag = true;
            @Override
            public void run() {
                while (timeOverFlag){
                    try {
                        // 매 5초마다 확인
                        Thread.sleep(5000);
                    }catch (Exception e){e.printStackTrace();}

                    int targetTotal = getTargetTotal();
                    Log.d("target total ",targetTotal+"");
                    if(targetTotal > over_time) {
                        timeOverFlag = false;
                        // 서비스 종료
                        stopSelf();
                    }
                }
                // 시간 초과 화면 시작
                Intent i = new Intent(getApplicationContext(),TimeOut.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent p = PendingIntent.getActivity(getApplicationContext(), 0, i, 0);
                try {
                    p.send();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        };

        Thread thread = new Thread(mRunnable);
        thread.start();


        return START_REDELIVER_INTENT;
    }
    // shared data hashmap list 타겟 전체 값 구하기
    public int getTargetTotal(){
        int sum_time = 0;
        final ArrayList<String> targets = SharedPreference.getStringArrayPref(this,"urls");
        for(int i=0; i<SharedData.hashMapArrayList.size(); i++){
            for (int j=0; j<targets.size(); j++){
                //Log.d("shared package ",SharedData.hashMapArrayList.get(i).get("packageName"));
                if(SharedData.hashMapArrayList.get(i).get("packageName").equals(targets.get(j))){
                    long l_runTime = Long.parseLong(SharedData.hashMapArrayList.get(i).get("runTime"));
                    int i_runTime = (int)l_runTime;

                    //Log.d("runtime ",runTime+"");
                    sum_time += i_runTime;
                    Log.d("sum_time",""+sum_time);
                }
            }
        }// end of for i
        return sum_time;
    }
}

