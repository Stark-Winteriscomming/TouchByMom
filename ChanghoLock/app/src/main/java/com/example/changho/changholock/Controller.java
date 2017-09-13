package com.example.changho.changholock;

import android.app.Activity;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Changho on 2016-05-24.
 */
public class Controller extends Activity {
    final static int RESULT_ENABLE = 1;
    ComponentName mDeviceAdminSample;
    Button enable;
    Button desable;
    static DevicePolicyManager policymanager;


    @Override
    protected void onResume() {
        super.onResume();
        updateButtonStates();
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDeviceAdminSample = new ComponentName(Controller.this, Device_Admin_Test.class);
        policymanager = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);

        setContentView(R.layout.controller);

        enable = (Button)findViewById(R.id.btn_active);
        desable = (Button)findViewById(R.id.btn_inactive);

        enable.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                        mDeviceAdminSample);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                        "Additional text explaining why this needs to be added.");
                startActivityForResult(intent, 1);

            }
        });

        desable.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                policymanager.removeActiveAdmin(mDeviceAdminSample);
                updateButtonStates();
            }
        });


    }

    public void updateButtonStates() {
        boolean active = policymanager.isAdminActive(mDeviceAdminSample);
        if (active) {
            enable.setEnabled(false);
            desable.setEnabled(true);
        } else {
            enable.setEnabled(true);
            desable.setEnabled(false);
        }
    }

    public static class Device_Admin_Test extends DeviceAdminReceiver {

        static SharedPreferences getSamplePreferences(Context context) {
            return context.getSharedPreferences(DeviceAdminReceiver.class.getName(), 0);
        };

        void showToast(Context context, CharSequence msg) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onEnabled(Context context, Intent intent) {
            showToast(context, "Sample Device Admin: enabled");
        }
        @Override
        public CharSequence onDisableRequested(Context context, Intent intent) {
            return "This is an optional message to warn the user about disabling.";
        }
        @Override
        public void onDisabled(Context context, Intent intent) {
            showToast(context, "Sample Device Admin: disabled");
        }
        @Override
        public void onPasswordChanged(Context context, Intent intent) {
            showToast(context, "Sample Device Admin: pw changed");
        }
        @Override
        public void onPasswordFailed(Context context, Intent intent) {
            showToast(context, "Sample Device Admin: pw failed");
        }
        @Override
        public void onPasswordSucceeded(Context context, Intent intent) {
            showToast(context, "Sample Device Admin: pw succeeded");
        }

    }

}
