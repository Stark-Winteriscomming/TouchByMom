package com.example.changho.changholock;

import android.graphics.drawable.Drawable;

import java.text.Collator;
import java.util.Comparator;

/**
 * Created by Changho on 2016-11-07.
 */

public class TimeOutListData {
    public String appName;
    public Drawable appIcon;
    public String app_use_time;


    // 알파벳 순서로 정렬
    public static final Comparator<TimeOutListData> ALPHA_COMPARATOR = new Comparator<TimeOutListData>(){
        private final Collator sCollator = Collator.getInstance();

        @Override
        public int compare(TimeOutListData mListDate_1,TimeOutListData mListDate_2){
            return sCollator.compare(mListDate_1.appName,mListDate_2.appName);
        }
    };
}
