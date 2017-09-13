package com.example.changho.changholock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Changho on 2016-05-31.
 */
public class NotificationActivity extends Activity{
    TextView targetApp;
    TextView LimitedTime;
    Button btn_apply;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        final Intent serviceIntent = new Intent(this, TimerService.class);
        targetApp = (TextView)findViewById(R.id.mTarget);
        LimitedTime = (TextView)findViewById(R.id.mTime);

        final Intent home_intent = new Intent(Intent.ACTION_MAIN);
        home_intent.addCategory(Intent.CATEGORY_HOME);
        home_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Intent얻기
        Bundle extras = getIntent().getExtras();
        //final Intent intent = getIntent();
        if (extras != null) {
            targetApp.setText(extras.getString("targetName"));
            LimitedTime.setText(extras.getString("LimitedTime"));
        }

        btn_apply = (Button)findViewById(R.id.btn_apply);
        btn_apply.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "터치시 뜨는말", 0).show();
                //Start Service
                startService(serviceIntent);
                startActivity(home_intent);
            }

        });

    }
}
