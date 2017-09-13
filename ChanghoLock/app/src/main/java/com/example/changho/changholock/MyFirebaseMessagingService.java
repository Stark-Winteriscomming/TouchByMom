package com.example.changho.changholock;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Changho on 2016-10-09.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("fcm message data ",remoteMessage.getData().toString());
        Map<String, String> params = remoteMessage.getData();
        //Map<String, Object> params = remoteMessage.getData();
        ArrayList<String> arrayList = new ArrayList<String>();

        try {
            JSONObject json = new JSONObject(params);
            Log.d("received string ",json.toString());

            //
            String time = json.getString("time");
            Log.d("time ",time);
            //
            JSONArray jsonArray = new JSONArray(json.getString("applist"));
            //Log.d("json array.get(0) ",(String)jsonArray.get(0));
            // sharedPreferce update
            for(int i=0; i<jsonArray.length(); i++){
                arrayList.add((String)jsonArray.get(i));
            }
            SharedPreference.setStringArrayPref(this, "urls",arrayList);

            // time 값에 따라 따른 동작
            if(time.equals("0")) {
                sendPushNotification("부모님이 타겟을 변경했습니다.", "0");
                // 서비스 실행
                Intent mainServiceIntent = new Intent(this,MainService.class);
                startService(mainServiceIntent);
            }else {
                //기존 잠금 리스트 저장
                SharedPreference.setStringArrayPref(this, "past_locked_apps", arrayList);
                sendPushNotification("부모님이 시간을 보내왔습니다. 시간:" + time, time);
                // isLocked 플래그 변경
                SharedData.isLocked = false;
            }
        }catch (Exception e){e.printStackTrace();}
    }

    /**
     * fcm 데이터 리시브 -> 받은 데이터로 알림창 띄우기
     */
    private void sendPushNotification(String message,String time) {
        System.out.println("received message : " + message);
        Intent intent = new Intent(this, ParentsTimeViewer.class);
        intent.putExtra("time",time);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.childicon).setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.childicon) )
                .setContentTitle("kidManagement")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri).setLights(000000255,500,2000)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakelock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        wakelock.acquire(5000);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}

