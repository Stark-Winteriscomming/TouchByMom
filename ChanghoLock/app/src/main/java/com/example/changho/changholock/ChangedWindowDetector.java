package com.example.changho.changholock;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Changho on 2016-10-05.
 */
public class ChangedWindowDetector extends AccessibilityService{
    String formerApp;
    String currentApp;
    String formerTime;


    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            Log.i("WindowChangeDetect ",
                    "Window Package: " + accessibilityEvent.getPackageName());

//            long now = System.currentTimeMillis();
//            Date date = new Date(now);
//            SimpleDateFormat sdfNow = new SimpleDateFormat("yyyyMMddHHmmss");
//            sdfNow.format(date);
//            Log.d("second diff "," "+getTime("20161025085000",sdfNow.format(date)));


            currentApp = ""+accessibilityEvent.getPackageName();
            final String home = "com.sec.android.app.launcher";

            // flag
            if(SharedData.flag) {

                // case: app1 -> app2
                if ((!(SharedData.formerApp.equals(home)))
                        &&(!(SharedData.formerApp.equals(currentApp))) && (!(home.equals(currentApp)))) {

                    Log.d("app1 -> ","app2");
                    // app1 시간 정보 기록
                    long timeGap = getTime(SharedData.formerTime, getCurrnetTimeString());

                    //
                    int index = SharedData.isEnclosed(SharedData.formerApp);
                    // 패키지 이름으로 해쉬맵에 저장된 실행시간을 꺼낸다.
                    String pastTime = SharedData.hashMapArrayList.get(index).get("runTime");
                    // 과거 시간
                    Long l_pastTime = Long.valueOf(pastTime);
                    // 현재시간을 더한 총 누적시간
                    Long sum_time = l_pastTime + timeGap;
                    // 총 누적시간을 해쉬맵에 더한다.
                    SharedData.hashMapArrayList.get(index).put("runTime", Long.toString(sum_time));

                    // app2 정보 저장
                    SharedData.formerApp = currentApp;
                    SharedData.formerTime = getCurrnetTimeString();

                    // app2 정보 해쉬맵에 존재하는지 체크
                    // 존재하지 않는다면
                    if (SharedData.isEnclosed(currentApp) < 0) {
                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        hashMap.put("packageName", currentApp);
                        hashMap.put("runTime", Long.toString(0));
                        SharedData.hashMapArrayList.add(hashMap);
                    }

                }
                // case: app1 -> home
                else if (!(SharedData.formerApp.equals(currentApp)) && currentApp.equals(home)) {
                    Log.d("app1 -> ","home");
                    long timeGap = getTime(SharedData.formerTime, getCurrnetTimeString());
                    // timeGap map에 저장
                    int index = SharedData.isEnclosed(SharedData.formerApp);
                    String pastTime = SharedData.hashMapArrayList.get(index).get("runTime");

                    long l_pastTime = Long.valueOf(pastTime);
                    long sum_time = l_pastTime + timeGap;

                    SharedData.hashMapArrayList.get(index).put("runTime", Long.toString(sum_time));

                    //home 정보 삽입
                    SharedData.formerApp = home;

                }
                // case: home -> app1
                else if (SharedData.formerApp.equals(home) && !(currentApp.equals(home))) {
                    Log.d("home -> ","app1");

                    SharedData.formerTime = getCurrnetTimeString();

                    // app1 정보 해쉬맵에 존재하는지 체크
                    // 존재하지 않는다면
                    if (SharedData.isEnclosed(currentApp) < 0) {
                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        hashMap.put("packageName", currentApp);
                        hashMap.put("runTime", Long.toString(0));
                        SharedData.hashMapArrayList.add(hashMap);
                    }
                    SharedData.formerApp = currentApp;
                }
            }
        }
    }
    // 현재 시간 문자열로
    static public String getCurrnetTimeString(){
        String s;
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyyMMddHHmmss");
        s = sdfNow.format(date);
        return s;
    }
    // long 형을 날짜 시간 문자열로 변경
    public String getTimeFormatedString(long timeMiles){
        String s;
        Date date = new Date(timeMiles);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyyMMddHHmmss");
        s = sdfNow.format(date);
        return s;
    }
    @Override
    public void onInterrupt() {

    }

    // TODO: 2016-10-25
    // second 단위로 리턴
    public long getTime(String start, String end) {
        Calendar cal01 = Calendar.getInstance();
        Calendar cal02 = Calendar.getInstance();

        cal01.set(
                Integer.parseInt(start.substring(0,4)),
                Integer.parseInt(start.substring(4,6)),
                Integer.parseInt(start.substring(6,8)),
                Integer.parseInt(start.substring(8,10)),
                Integer.parseInt(start.substring(10,12)),
                Integer.parseInt(start.substring(12,14))
        );
        cal02.set(
                Integer.parseInt(end.substring(0,4)),
                Integer.parseInt(end.substring(4,6)),
                Integer.parseInt(end.substring(6,8)),
                Integer.parseInt(end.substring(8,10)),
                Integer.parseInt(end.substring(10,12)),
                Integer.parseInt(end.substring(12,14))
        );

        long time = (cal02.getTime().getTime() - cal01.getTime().getTime())/1000;

        return time;
    }
}
