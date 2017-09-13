package com.example.changho.changholock;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;





/**
 * Created by Changho on 2016-05-03.
 */
public class TargetedApp extends Activity{
    private ListView mTListView = null;
    private ListViewAdapter mTAdapter = null;
    private Button btn;
    ArrayList<String> target_list;
    String pn;
    //패키지 네임 저장 변수
    private ArrayList<String> lockAppList = new ArrayList<String>();
    //기존 targetedApp 저장 testing
    //Bundle b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.targeted_app);
        //
        mTListView = (ListView) findViewById(R.id.mListView);
        //타겟앱을 고르고 적용하는 버튼
        //btn = (Button)findViewById(R.id.btn_add);

        mTAdapter = new ListViewAdapter(this);
        mTListView.setAdapter(mTAdapter);
        // 다중 선택 가능하게
        mTListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        final Intent appShowIntent = new Intent(this, InstalledAppShow.class);

        //
        final PackageManager pm = this.getApplicationContext().getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> list = pm.queryIntentActivities(intent, 0);
        //
        //Intent targettingIntent = getIntent();
        //target_list = targettingIntent.getStringArrayListExtra("list");
        //if(target_list != null)
        //original_target_list = target_list;

        target_list = SharedPreference.getStringArrayPref(this,"urls");
        if(target_list != null)
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
                mTAdapter.addItem(iconDrawable, name, target);
            }
            catch (PackageManager.NameNotFoundException e)
            {
                return;
            }
            //input AppData to list , checkbox는 어떻게 넣을까?

                //lockAppList.add(pName);


        //com.korail.korail com.intsig.camscanner
        } //end of for

        //adapter click event testing
        mTListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View view, int position,
                                    long id) {
                // TODO Auto-generated method stub

                ListTData mData = mTAdapter.mListData.get(position);
                String package_name = mData.mPName;
                pn =    package_name;
                //삭제 다이어로그 testing
                AlertDialog.Builder builder = new AlertDialog.Builder(TargetedApp.this);     // 여기서 this는 Activity의 this

// 여기서 부터는 알림창의 속성 설정
                builder.setTitle("타겟 삭제 대화 상자")        // 제목 설정
                        .setMessage("삭제하시겠습니까?")        // 메세지 설정
                        .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                        .setPositiveButton("확인", new DialogInterface.OnClickListener(){
                            // 확인 버튼 클릭시 설정
                            public void onClick(DialogInterface dialog, int whichButton){
                                //MainService로 인텐트 보내기
                                final Intent serviceIntent = new Intent(TargetedApp.this, MainService.class);
                                lockAppList.add(pn);
                                serviceIntent.putExtra("dList",lockAppList);
                                startService(serviceIntent);
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener(){
                            // 취소 버튼 클릭시 설정
                            public void onClick(DialogInterface dialog, int whichButton){
                                dialog.cancel();
                            }
                        });

                AlertDialog dialog = builder.create();    // 알림창 객체 생성
                dialog.show();
                // end of 삭제 다이어로그



                Toast.makeText(TargetedApp.this,package_name,Toast.LENGTH_SHORT).show();
            }
        });
        // end of adapter click event testing



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

            //holder.mPText.setVisibility(View.GONE);
            //

            //checkBox의 클릭여부를 listView가 관리하게 한다.
            //holder.mCheck.setChecked(((ListView) parent).isItemChecked(position));
            //
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
//        public void sort(){
//            Collections.sort(mListData, ListTData.ALPHA_COMPARATOR);
//            dataChange();
//        }
        public void dataChange(){
            mTAdapter.notifyDataSetChanged();
        }
//여기 까지
    }
}
