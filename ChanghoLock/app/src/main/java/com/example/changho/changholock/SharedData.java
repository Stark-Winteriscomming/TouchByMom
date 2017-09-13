package com.example.changho.changholock;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Changho on 2016-09-19.
 */

/**
 * 모든 클래스가 참조하는 타겟리스트 목록
 */
public class SharedData {
     // 부모가 시간을 부여했나 아닌가를 검사하는 플레그 -- main 서비스가 참조한다.
     static boolean isLocked = true;
     static ArrayList<String> targetedList = new ArrayList<>();
     static String currentActivity;
     static ArrayList<String> installedApps = new ArrayList<>();
     // TODO: 2016-10-25
     static long currentTime = 0;
     static String formerApp;
     static String formerTime;
     static Boolean flag = false;
     static int i = 0;

     //hashmap
     static ArrayList<HashMap<String,String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();

     /**
      * list에 있는지 확인하는 메소드
      *
      */

     static public int isEnclosed(String packageName){
          for(int i=0; i<hashMapArrayList.size(); i++) {
               if(hashMapArrayList.get(i).get("packageName").equals(packageName)){
                    return i;
               }
          }
          return -1;
     }

}
