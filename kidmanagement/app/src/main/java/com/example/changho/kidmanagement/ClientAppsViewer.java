package com.example.changho.kidmanagement;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.changho.kidmanagement.service.DbService;
import com.example.changho.kidmanagement.service.SendTargets;
import com.example.changho.kidmanagement.utils.SharedData;
import com.example.changho.kidmanagement.utils.Utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 *
 * 아이 측 설치된 앱 정보 보기
 *
 */
public class ClientAppsViewer extends AppCompatActivity {

    private ListView mListView = null;
    private ListViewAdapter mAdapter = null;
    private Button btn_apply;
    private EditText editTime;
    private DbService dbService;
    //

    private ArrayList<String> lockAppList = new ArrayList<String>();


    //private PackageManager pm=null;
    //private List<ApplicationInfo> mAppList = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_show);

        mListView = (ListView) findViewById(R.id.mList);
        //타겟앱을 고르고 적용하는 버튼
        btn_apply = (Button)findViewById(R.id.btn_apply);

        mAdapter = new ListViewAdapter(this);
        mListView.setAdapter(mAdapter);
        //
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        dbService = new DbService(this);

        /**
         *
         * Using PackageManager
         *
         */


        String path = Environment.getExternalStorageDirectory().toString()+"/appicon/";

        ArrayList<HashMap<String,String>> appList =dbService.selectAppInfo();
        for(int i=0; i<appList.size(); i++){
            String name = appList.get(i).get("app_name");
            String pName = appList.get(i).get("app_package");
            String appIconPath = path+pName+".jpg";
            Drawable icon = Drawable.createFromPath(appIconPath);
            mAdapter.addItem(icon, name, pName);
        }


        /**
         * select method returns app's info
         */
        ArrayList<HashMap<String, String>> appsInfo = new ArrayList<>();
        appsInfo = dbService.selectAppInfo();

        for(HashMap<String, String> hashMap : appsInfo){
            Log.i("appname ",hashMap.get("app_name"));
            Log.i("packagename ",hashMap.get("app_package"));
        }


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View view, int position,
                                    long id) {
                // TODO Auto-generated method stub

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


                Toast.makeText(ClientAppsViewer.this,package_name,Toast.LENGTH_SHORT).show();
            }
        });


        btn_apply.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int size = mAdapter.getCount();
                for(String name: lockAppList){
                    Log.i("applist :",name);
                }
            //
                Utils.setStringArrayPref(ClientAppsViewer.this, "targetApps", lockAppList);
                //
                new SendTargets(getApplicationContext(),"0").execute();
                // 메인액티비티로 복귀
                Intent mainIntent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(mainIntent);
            }

        });
        //
    }  // end of onCreate
    private class ViewHolder {
        public ImageView mIcon;
        public TextView mText;
        public CheckBox mCheck;
        public TextView mPText;
    }

    private class ListViewAdapter extends BaseAdapter {
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
                convertView = inflater.inflate(R.layout.app_list_item,null);


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
