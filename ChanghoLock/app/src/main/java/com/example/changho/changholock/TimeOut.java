package com.example.changho.changholock;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by changho on 2016-04-12.
 */
// 시간초과 알림 액티비티
public class  TimeOut extends Activity {
    private ListView listView_timeout=null;
    private ListViewAdaptor listViewAdaptor = null;
    private Button btn_exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_out);

        // button
        btn_exit = (Button)findViewById(R.id.BUTTON_EXIT);
        // listview
        listView_timeout = (ListView)findViewById(R.id.TIME_OUT_LISTVIEW);
        listViewAdaptor = new ListViewAdaptor(this);
        listView_timeout.setAdapter(listViewAdaptor);
        listView_timeout.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        btn_exit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final Intent home_intent = new Intent(Intent.ACTION_MAIN);
                home_intent.addCategory(Intent.CATEGORY_HOME);
                home_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(home_intent);
            }
        });
        SharedData.isLocked = true;
        ArrayList<String> target_list = SharedPreference.getStringArrayPref(this,"urls");
        for (int i=0; i<target_list.size(); i++) {
            String target = target_list.get(i);
            //String name = String.valueOf(applicationInfo.loadLabel(pm));    // 앱 이름
            //String pName = applicationInfo.activityInfo.packageName;   // 앱 패키지
            try
            {
                final PackageManager p = getApplicationContext().getPackageManager();
                //ApplicationInfo ai;
                Drawable iconDrawable = p.getApplicationIcon(target);
                //
                ApplicationInfo ai;
                try { ai = p.getApplicationInfo(target, 0); }
                catch (final PackageManager.NameNotFoundException e) { ai = null; }
                final String name = (String) (ai != null ? p.getApplicationLabel(ai) : "(unknown)");
                //adding to Adapter
                // hashmap list에서 target packagename search
                int i_info = 0;
                for(int j=0; j<SharedData.hashMapArrayList.size(); j++){
                   if(target.equals(SharedData.hashMapArrayList.get(j).get("packageName"))){
                        i_info = j;
                   }
                }
                String str_runTime = SharedData.hashMapArrayList.get(i_info).get("runTime");
                int i_time = Integer.parseInt(str_runTime);
                String str_time = ""+(i_time / 3600) + "시" + (i_time % 3600 / 60) + "분" + (i_time % 3600 % 60) + "초";
                listViewAdaptor.addItem(iconDrawable, name,str_time);
            }
            catch (PackageManager.NameNotFoundException e)
            {
                return;
            }
        } //end of for
        SharedData.hashMapArrayList.clear();
    } // end of onCreate
    // viewHolder
    private class TimeOutViewHolder{
        public TextView appName;
        public ImageView appIcon;
        public TextView appUseTime;
    }

    public class ListViewAdaptor extends BaseAdapter{
        private Context context = null;
        private ArrayList<TimeOutListData> mListData= new ArrayList<TimeOutListData>();

        public ListViewAdaptor(Context context) {
            super();
            this.context = context;
        }

        @Override
        public int getCount(){
            return mListData.size();
        }
        @Override
        public Object getItem(int position){
            return mListData.get(position);
        }
        @Override
        public long getItemId(int position){
            return position;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            TimeOutViewHolder holder;
            Log.d("getview","getview");
            //prevention for generating view
            if(view == null){
                holder = new TimeOutViewHolder();

                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.time_out_list_item,null);


                holder.appIcon = (ImageView)view.findViewById(R.id.mIcon);
                holder.appName = (TextView)view.findViewById(R.id.mText);
                holder.appUseTime = (TextView) view.findViewById(R.id.mTime);
                view.setTag(holder);

            }else{
                holder = (TimeOutViewHolder) view.getTag();
            }
            TimeOutListData mData = mListData.get(i);

            if(mData.appIcon != null){
                holder.appIcon.setVisibility(View.VISIBLE);
                holder.appIcon.setImageDrawable(mData.appIcon);
            }else{
                holder.appIcon.setVisibility(View.GONE);
            }
            holder.appName.setText(mData.appName);
            holder.appUseTime.setText(mData.app_use_time);

            return view;
        }

        public void addItem(Drawable icon , String appName , String appUseTime){
            TimeOutListData addInfo = null;

            addInfo = new TimeOutListData();
            addInfo.appIcon = icon;
            addInfo.appName = appName;
            addInfo.app_use_time = appUseTime;
            mListData.add(addInfo);
        }
        public void remove(int position){
            mListData.remove(position);
            dataChange();
        }
        public void sort(){
            Collections.sort(mListData, TimeOutListData.ALPHA_COMPARATOR);
            dataChange();
        }
        public void dataChange(){
            listViewAdaptor.notifyDataSetChanged();
        }
    }     // end of adaptor
}

