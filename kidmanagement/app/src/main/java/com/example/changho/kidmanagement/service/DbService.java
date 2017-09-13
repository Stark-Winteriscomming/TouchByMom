package com.example.changho.kidmanagement.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.changho.kidmanagement.utils.Utils.updateSingleQuote;

/**
 * Created by Changho on 2016-11-01.
 */

public class DbService {
    private MySQLiteOpenHelper helper;
    String dbName = "appinfo.db";
    int dbVersion = 1; // 데이터베이스 버전
    private SQLiteDatabase db;
    String tag = "SQLite"; // Log 에 사용할 tag


    // context 전달 필요
    public DbService(Context context) {
        helper = new MySQLiteOpenHelper(context,dbName,null,dbVersion);

        try {
            db = helper.getWritableDatabase(); // 읽고 쓸수 있는 DB
            //db = helper.getReadableDatabase(); // 읽기 전용 DB select문
        } catch (SQLiteException e) {
            e.printStackTrace();
            Log.e(tag, "데이터베이스를 얻어올 수 없음");
        }
    }

    public void delete() {
    }
    public void deleteAll(){
        db.execSQL("delete from apptable");
        Log.d(tag,"deleteAll success");
    }
    public void update() {
    }

    // 여러 app 정보를 hashmap에 저장
    public ArrayList<HashMap<String,String>> selectAppInfo() {
        Cursor c = db.rawQuery("select appname,packagename from apptable;", null);
        ArrayList<HashMap<String,String>> app_info_list = new ArrayList<>();
        while(c.moveToNext()) {
            HashMap<String,String> hashMap = new HashMap();
            String name = c.getString(0);
            String packagename = c.getString(1);

            hashMap.put("app_name", name);
            hashMap.put("app_package", packagename);
            app_info_list.add(hashMap);

            Log.d(tag,"name:"+name+",package:"+packagename);

        }
        return app_info_list;
    }

    public void selectAll(){
        int i=0;
        Cursor c = db.rawQuery("select DISTINCT id,appname,packagename from apptable;", null);
        while(c.moveToNext()) {

            int id = c.getInt(0);
            String name = c.getString(1);
            String packagename = c.getString(2);
            i++;
            Log.d(tag,"name:"+name+",package:"+packagename);

        }
        Log.d("count ",""+i);
    }

    public void insert (String appName , String packageName) {
        db.execSQL("insert into apptable (appname , packagename) values( '"+appName+"', '"+packageName+"');");
        Log.d(tag, "**insert completed**");
    }

    public void insertApps(ArrayList<HashMap<String,String>> appList){
//        db.execSQL("insert into apptable (appname , packagename) values( '"+appName+"', '"+packageName+"');");
        final String FIRST_STRING = "insert into apptable (appname , packagename) values";
        final String LAST_STRING  = ")";
        String midString="";
        String insertQuery;

        for(int i=0; i<(appList.size())-1; i++){
            String appName = appList.get(i).get("appName");
            String packageName = appList.get(i).get("packageName");

            //adding
            if(appName.indexOf('\'') >= 0){
                appName = updateSingleQuote(appName);
            }

            midString += "( '"+appName+"', ";
            midString += " '"+packageName+"' ), ";



        }
        // 마지막 string
        String appName = appList.get(appList.size()-1).get("appName");
        String packageName = appList.get(appList.size()-1).get("packageName");
        midString += "( '"+appName+"', ";
        midString += " '"+packageName+"'  ";

        insertQuery = FIRST_STRING + midString +LAST_STRING;
        db.execSQL(insertQuery);

    //(1, 'Smith', 'John', 'TechOnTheNet.com'),
    }
}
