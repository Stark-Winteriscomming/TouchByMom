package com.example.changho.changholock;

/**
 * Created by changho on 2016-03-27.
 */
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainService extends Service {

    Thread mThread;
    ArrayList<String> list;
    ArrayList<String> dList;
    static Boolean check = false;
    InstalledAppReceiver installedAppReceiver;

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        // 서비스 정지돼도 다시 생성 (예외 발생했음: 메인서비스가 아닌 다른 서비스가 같이 실행되어 있을 때 )
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000, restartServicePendingIntent);

        super.onTaskRemoved(rootIntent);
    }

    public MainService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onCreate(){
        Toast.makeText(this, "Service start", Toast.LENGTH_LONG).show();

        installedAppReceiver = new InstalledAppReceiver();
        registerReceiver(installedAppReceiver, new IntentFilter(
                Intent.ACTION_PACKAGE_ADDED));
    }
    @Override
    public void onDestroy() {
        Log.d("service"," destroyed");
        // 0928
        unregisterReceiver(installedAppReceiver);
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
    //me: onStratCommand에 주 수행 작업을 코딩하는 듯
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        //intent값 받기
        super.onStartCommand(intent, flags, startId);
        //list = intent.getStringArrayListExtra("list");

        dList = intent.getStringArrayListExtra("dList");
        if(dList != null)
        Log.i("dList 값" , dList.get(0));
        //디바이스매니저 잠금 테스팅

        // targetList 얻기
        list = SharedPreference.getStringArrayPref(this, "urls");
         //testing
        if(SharedData.targetedList != null) {
            list.add("com.google.android.apps.adm");

            for (int i = 0; i < list.size(); i++) {
                String target = list.get(i);
                Log.d("target check onService ", target);
            }


            //confirm process
            mThread = new Thread(worker);
            mThread.setDaemon(true);
            mThread.start();
        }

        return START_REDELIVER_INTENT;
    }
    Runnable worker = new Runnable()
    {
        public void run()
        {
            runLog();
        }
    };




    //

    ///////runLog 부분
    private void runLog() {
        // TODO Auto-generated method stub
                boolean exitFlag = true;
                List<ActivityManager.RunningTaskInfo> info;
                //잠금 화면 intent
                Intent intent = new Intent(this, LockScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                //Intent intent = new Intent(getApplicationContext(), LockScreen.class);
                while (exitFlag) {
                    //Log.d("service live","service live");
                    ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                    info = activityManager.getRunningTasks(1);

                    // 현재 액티비티
                    String str = info.get(0).topActivity.getPackageName();



                    //testing --- class name???
                    String deviceAdmin = info.get(0).topActivity.getClassName();
                    //Log.e("현재 실행중인 액티비티 : ",deviceAdmin);

                    /**
                     * isLocked flag true-> 잠김 flag false->풀어짐
                     */
                    if(SharedData.isLocked) {
                        if (list != null)
                            for (int i = 0; i < SharedPreference.getStringArrayPref(this, "urls").size(); i++) {
                                String target = SharedPreference.getStringArrayPref(this, "urls").get(i);
                                if (str.equals(target) || deviceAdmin.equals("com.android.settings.DeviceAdminAdd")) {
                                    startActivity(intent);
                                }
                            }
                    }
                } //end of while
    }

}

