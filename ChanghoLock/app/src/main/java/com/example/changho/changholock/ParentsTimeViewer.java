package com.example.changho.changholock;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ParentsTimeViewer extends AppCompatActivity {
    private ListView mTListView = null;
    private ListViewAdapter mTAdapter = null;
    private Button btn_apply;
    ArrayList<String> target_list;
    private TextView timeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents_time_viewer);

        timeText  = (TextView) findViewById(R.id.LIMIT_TIME);

        mTListView = (ListView) findViewById(R.id.mListView);
        //타겟앱을 고르고 적용하는 버튼
        btn_apply = (Button)findViewById(R.id.btn_time_apply);

        mTAdapter = new ListViewAdapter(this);
        mTListView.setAdapter(mTAdapter);
        // 다중 선택 가능하게
        mTListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        /**
         * 부모로 부터 받은 시간을 적용시킨다(타겟 앱 사용 시작)
         */
        Intent intent = getIntent();
        final String time  = intent.getExtras().getString("time");

        if(time.equals("0")){
            timeText.setVisibility(View.INVISIBLE);
            btn_apply.setText("확인");
        }else {
            //초를 시분초로 환산
            int i_time = Integer.parseInt(time);
            String str_time = ""+(i_time / 3600) + "시" + (i_time % 3600 / 60) + "분" + (i_time % 3600 % 60) + "초";
            timeText.setText("추가 시간: " + str_time);
        }


        btn_apply.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "터치시 뜨는말", 0).show();
                //Start Service
                if(time.equals("0")){
                    final Intent home_intent = new Intent(Intent.ACTION_MAIN);
                    home_intent.addCategory(Intent.CATEGORY_HOME);
                    home_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(home_intent);
                }
                else {
                    Log.d("check Button clicked", " ");
                    List<ActivityManager.RunningTaskInfo> info;
                    ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                    info = activityManager.getRunningTasks(1);

                    // 현재 액티비티 패키지명
                    String packageNow = info.get(0).topActivity.getPackageName();
                    Log.d("package in button click", packageNow);

                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("packageName", packageNow);
                    hashMap.put("runTime", Long.toString(0));

                    SharedData.hashMapArrayList.add(hashMap);
                    SharedData.formerApp = packageNow;
                    SharedData.formerTime = ChangedWindowDetector.getCurrnetTimeString();
                    SharedData.flag = true;

                    //타이머 서비스 스타트
                    Intent timerServiceIntent = new Intent(getApplicationContext(), TimerService.class);
                    timerServiceIntent.putExtra("time", time);
                    startService(timerServiceIntent);

                    //home으로
                    final Intent home_intent = new Intent(Intent.ACTION_MAIN);
                    home_intent.addCategory(Intent.CATEGORY_HOME);
                    home_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(home_intent);
                }
            }

        });
        //
        final PackageManager pm = this.getApplicationContext().getPackageManager();
        Intent filter_intent = new Intent(Intent.ACTION_MAIN);
        filter_intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> list = pm.queryIntentActivities(filter_intent, 0);

        target_list = SharedPreference.getStringArrayPref(this,"urls");
        if(target_list != null)
            for (int i=0; i<target_list.size(); i++) {
                String target = target_list.get(i);

                try
                {
                    final PackageManager p = getApplicationContext().getPackageManager();
                    Drawable iconDrawable = p.getApplicationIcon(target);
                    //
                    ApplicationInfo ai;
                    try { ai = p.getApplicationInfo(target, 0); }
                    catch (final PackageManager.NameNotFoundException e) { ai = null; }
                    final String name = (String) (ai != null ? p.getApplicationLabel(ai) : "(unknown)");

                    //adding to Adapter
                    mTAdapter.addItem(iconDrawable, name, target);
                }
                catch (PackageManager.NameNotFoundException e)
                {
                    return;
                }

            } //end of for
    }
    //on Resume
    protected void onRestart(){
        super.onRestart();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    private class ViewHolder {
        public ImageView mIcon;
        public TextView mText;
        public TextView mPText;
    }

    private class ListViewAdapter extends BaseAdapter {
        private Context mContext = null;
        private ArrayList<ListTData> mListData= new ArrayList<ListTData>();

        public ListViewAdapter(Context mContext){
            super();
            this.mContext = mContext;
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
        public View getView(int position , View convertView , ViewGroup parent){
            ViewHolder holder;
            //prevention for generating view
            if(convertView == null){
                holder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.targeted_app_list_item,null);
                holder.mIcon = (ImageView)convertView.findViewById(R.id.mImage);
                holder.mText = (TextView)convertView.findViewById(R.id.mText);
                convertView.setTag(holder);

            }else{
                holder = (ViewHolder)convertView.getTag();
            }
            ListTData mData = mListData.get(position);

            if(mData.mIcon != null){
                holder.mIcon.setVisibility(View.VISIBLE);
                holder.mIcon.setImageDrawable(mData.mIcon);
            }else{
                holder.mIcon.setVisibility(View.GONE);
            }
            holder.mText.setText(mData.mTitle);

            return convertView;
        }

        /**
         * Adapter에게 필수는 아니지만 사용하면서 필요한 메소드
         */
        public void addItem(Drawable icon , String mTitle , String mPName){
            ListTData addInfo = null;

            addInfo = new ListTData();

            addInfo.mIcon = icon;
            addInfo.mTitle = mTitle;
            addInfo.mPName = mPName;
            mListData.add(addInfo);
        }
        public void remove(int position){
            mListData.remove(position);
            dataChange();
        }

        public void dataChange(){
            mTAdapter.notifyDataSetChanged();
        }
//여기 까지
    }
}
