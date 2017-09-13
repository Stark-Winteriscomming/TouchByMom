package com.example.changho.changholock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by changho on 2016-04-02.
 */
public class InstalledAppShow extends Activity {

    private ListView mListView = null;
    private ListViewAdapter mAdapter = null;
    private Button btn_apply;

    //

    private ArrayList<String> lockAppList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.installed_app_show);

        mListView = (ListView) findViewById(R.id.mList);
        //타겟앱을 고르고 적용하는 버튼
        btn_apply = (Button)findViewById(R.id.btn_apply);

        mAdapter = new ListViewAdapter(this);
        mListView.setAdapter(mAdapter);
        //
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


        /**
         *
         * Using PackageManager
         *
         */


         //패키지 매니저에서 설치된 앱 리스트 가져오기

        final PackageManager pm = this.getApplicationContext().getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> list = pm.queryIntentActivities(intent, 0);

        //
//        for (ResolveInfo applicationInfo : list) {
//            String name = String.valueOf(applicationInfo.loadLabel(pm));    // 앱 이름
//            String pName = applicationInfo.activityInfo.packageName;   // 앱 패키지
//            Drawable iconDrawable = applicationInfo.loadIcon(pm);   // 앱 아이콘
//            boolean flag = false;
//            //input AppData to list , checkbox는 어떻게 넣을까?
//            mAdapter.addItem(iconDrawable, name, pName);
//        }
        mAdapter.addItem(null,"app name","time");
        //
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> av, View view, int position,
                                        long id) {
                    // TODO Auto-generated method stub
                    //String app_name = ((TextView) view.findViewById(R.id.app_name)).getText().toString();
                    //String app_name = ((TextView) view.findViewById(R.id.mText)).getText().toString();
                    //String package_name = ((TextView) view.findViewById(R.id.mPText)).getText().toString();
                    ListData mData = mAdapter.mListData.get(position);
                    String package_name = mData.mPName;
                    //클릭시 체크가 되기 위해
                    CheckBox cb = (CheckBox)view.findViewById(R.id.mCheck);

                    if(cb.isChecked()==true) {
                        cb.setChecked(false);
                        //mData.mCheck.setChecked(false);
                        lockAppList.remove(package_name);
                    }
                    else {
                        cb.setChecked(true);
                        //mData.mCheck.setChecked(true);
                        lockAppList.add(package_name);
                    }


                    Toast.makeText(InstalledAppShow.this,package_name,Toast.LENGTH_SHORT).show();
                }
            });
        //여러앱 적용 테스트
        final Intent serviceIntent = new Intent(this, MainService.class);
        //testing
        final Intent targetedAppShowIntent = new Intent(this,TargetedApp.class);

        btn_apply.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int size = mAdapter.getCount();
                for(String name: lockAppList){
                    Log.i("applist :",name);
                }

                // 메인서비스 실행
                SharedPreference.setStringArrayPref(InstalledAppShow.this, "urls", lockAppList);
                startActivity(targetedAppShowIntent);
                startService(serviceIntent);
             }

        });
        //
    }  // end of onCreate
    private class ViewHolder {
        public ImageView mIcon;
        public TextView mText;
        public CheckBox mCheck;
        //
        public TextView mPText;
    }

    private class ListViewAdapter extends BaseAdapter{
        private Context mContext = null;
        private ArrayList<ListData> mListData= new ArrayList<ListData>();

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
                convertView = inflater.inflate(R.layout.installed_app_list_item,null);


                holder.mIcon = (ImageView)convertView.findViewById(R.id.mImage);
                holder.mText = (TextView)convertView.findViewById(R.id.mText);
                holder.mCheck = (CheckBox)convertView.findViewById(R.id.mCheck);
                holder.mPText = (TextView) convertView.findViewById(R.id.mPText);
                convertView.setTag(holder);

            }else{
                holder = (ViewHolder)convertView.getTag();
            }
            ListData mData = mListData.get(position);

            if(mData.mIcon != null){
                holder.mIcon.setVisibility(View.VISIBLE);
                holder.mIcon.setImageDrawable(mData.mIcon);
            }else{
                holder.mIcon.setVisibility(View.GONE);
            }
            //testing

            //
            holder.mText.setText(mData.mTitle);
            holder.mPText.setText(mData.mPName);

            //checkBox의 클릭여부를 listView가 관리하게 한다.
            holder.mCheck.setChecked(((ListView) parent).isItemChecked(position));
            holder.mCheck.setChecked(false);
            holder.mCheck.setFocusable(false);
            holder.mCheck.setClickable(false);
            //
            return convertView;
        }

        /**
         * Adapter에게 필수는 아니지만 사용하면서 필요한 메소드
         */
        public void addItem(Drawable icon , String mTitle , String mPName){
            ListData addInfo = null;

            addInfo = new ListData();

            addInfo.mIcon = icon;
            addInfo.mTitle = mTitle;
            addInfo.mPName = mPName;

            mListData.add(addInfo);
        }
        public void remove(int position){
            mListData.remove(position);
            dataChange();
        }
        public void sort(){
            Collections.sort(mListData, ListData.ALPHA_COMPARATOR);
            dataChange();
        }
        public void dataChange(){
            mAdapter.notifyDataSetChanged();
        }
    //여기 까지
    }

}
