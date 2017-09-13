package com.example.changho.kidmanagement;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.changho.kidmanagement.service.DbService;
import com.example.changho.kidmanagement.service.ManagementService;
import com.example.changho.kidmanagement.service.SendTargets;
import com.example.changho.kidmanagement.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TargetTimeWriter extends Fragment {
    private Button btn_apply;
    private int hours;
    private int minutes;
    private int seconds;
    private int time_to_seconds = 0;
    public String s_hours;
    private String s_minutes;
    private String s_seconds;
    private String strTime;

    EditText editHours;
    EditText editMinutes;
    EditText editSeconds;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.activity_target_time_writer,
//                container, false);
        final View rootView = getActivity().getLayoutInflater().inflate(R.layout.activity_target_time_writer,
                null);

        btn_apply = (Button)rootView.findViewById(R.id.APPLY_TIME);

        editHours = (EditText)rootView.findViewById(R.id.EDIT_HOURS);
        editMinutes = (EditText)rootView.findViewById(R.id.EDIT_MINUTES);
        editSeconds = (EditText)rootView.findViewById(R.id.EDIT_SECONDS);


        editHours.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                editHours.setText("");
            }
        });

        btn_apply.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.d("b","clicked");
                s_hours = ((EditText)rootView.findViewById(R.id.EDIT_HOURS)).getText().toString().trim();
                s_minutes = ((EditText)rootView.findViewById(R.id.EDIT_MINUTES)).getText().toString().trim();
                s_seconds = ((EditText)rootView.findViewById(R.id.EDIT_SECONDS)).getText().toString().trim();

                hours = Integer.parseInt(s_hours);
                minutes = Integer.parseInt(s_minutes);
                seconds = Integer.parseInt(s_seconds);

                time_to_seconds = (hours*3600)+(minutes*60)+seconds;
                Log.d("total seconds",time_to_seconds+"");
                strTime = Integer.toString(time_to_seconds);
                //
                new SendTargets(getActivity(),strTime).execute();
            }
        });
        return rootView;
    }
}
