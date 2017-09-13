package com.example.changho.changholock;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

/**
 * Created by changho on 2016-03-27.
 */
public class LockScreen extends AppCompatActivity{
        Button btn_exit;
        Button btn_unlock;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.lockscreen);
            //
//            Button btn_exit;
            btn_exit = (Button) findViewById(R.id.btn_out);
            final Intent home_intent = new Intent(Intent.ACTION_MAIN);
            final Intent serviceIntent = new Intent(this, TimerService.class);
            home_intent.addCategory(Intent.CATEGORY_HOME);
            home_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            //Intent for HOME
            btn_exit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    startActivity(home_intent);
                }
            });
            //intent for unlock , task : 1. start service 2. exit for Home



        }


}
