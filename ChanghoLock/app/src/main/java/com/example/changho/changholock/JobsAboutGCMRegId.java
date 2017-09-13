package com.example.changho.changholock;

import android.content.Context;
import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

/**
 * Created by Changho on 2016-10-07.
 */
public class JobsAboutGCMRegId {

    // sharedRef에 사용될 Registration Id Key값
    private static final String PROPERTY_REG_ID = "regId";
    private static final String TAG = "JobsAboutFCMRegId ";
    Context context;
    String regId;

    public JobsAboutGCMRegId(Context context) {
        this.context = context;
    }
    // debugging msg
    public void msg(String message){
        Log.d(TAG , message);
    }


}
