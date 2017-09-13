package com.example.changho.kidmanagement;

import android.graphics.drawable.Drawable;
import android.widget.CheckBox;

import java.text.Collator;
import java.util.Comparator;

/**
 * Created by Changho on 2016-11-01.
 */

public class ListData {
    public Drawable mIcon;
    // App name
    public String mTitle;
    public CheckBox mCheck;
    public String mPName;
    //생성자 추가해보기
//    public ListData(){
//        this.mCheck.setChecked(false);
//    }
    //order of Arphabet
    public static final Comparator<ListData> ALPHA_COMPARATOR = new Comparator<ListData>(){
        private final Collator sCollator = Collator.getInstance();

        @Override
        public int compare(ListData mListDate_1,ListData mListDate_2){
            return sCollator.compare(mListDate_1.mTitle,mListDate_2.mTitle);
        }
    };
}
