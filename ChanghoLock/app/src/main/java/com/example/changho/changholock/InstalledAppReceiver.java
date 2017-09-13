package com.example.changho.changholock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Changho on 2016-09-27.
 */
public class InstalledAppReceiver extends BroadcastReceiver{
    //

    @Override
    public void onReceive(Context context, Intent intent) {
        String appName = intent.getData().getSchemeSpecificPart();
        String action = intent.getAction();

        if(action.equals(Intent.ACTION_PACKAGE_ADDED))
        {
            Log.i("Added App name : " ,appName);
        }
        else if(action.equals(Intent.ACTION_PACKAGE_REMOVED))
        {
            Log.i("Removed App name : " , appName);
        }
    }
}
