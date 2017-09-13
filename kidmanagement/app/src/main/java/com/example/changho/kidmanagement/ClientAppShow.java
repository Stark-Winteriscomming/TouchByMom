package com.example.changho.kidmanagement;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.changho.kidmanagement.service.DbService;
import com.example.changho.kidmanagement.service.ManagementService;
import com.example.changho.kidmanagement.service.ManagementServiceImpl;
import com.example.changho.kidmanagement.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 *
 * 타겟으로 지정된 앱 보는 fragment
 *
 */
public class ClientAppShow extends Fragment {
    Context context;
    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    ManagementService managementService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private ListView mTListView = null;
    private ListViewAdapter mTAdapter = null;
    private Button btn;
    ArrayList<String> target_list;
    String pn;
    private DbService dbService;

    //패키지 네임 저장 변수
    private ArrayList<String> lockAppList = new ArrayList<String>();
    //기존 targetedApp 저장 testing
    static ArrayList<String> original_target_list;

    //
    public ClientAppShow() {

    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d("onResume","onResume");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_client_app_show,
                container, false);
        // testing
        Log.d("onCreate","onCreate" +
                "");
        //
        dbService = new DbService(getActivity());
        Button btn_select = (Button) rootView.findViewById(R.id.btn_select);
        String path = Environment.getExternalStorageDirectory().toString()+"/appicon/";
        //
        mTListView = (ListView)rootView.findViewById(R.id.mList2);

        //////////////////////////
        final PackageManager pm = getActivity().getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> list = pm.queryIntentActivities(intent, 0);

        target_list = Utils.getStringArrayPref(context, "targetApps");
        mTAdapter = new ListViewAdapter(getActivity());

        mTListView.setAdapter(mTAdapter);

        ArrayList<HashMap<String,String>> appList =dbService.selectAppInfo();


        if(target_list != null)
        for(String str :target_list)
        Log.d("target ",str);
        for(int i=0; i<appList.size(); i++){
            for(int j=0; j<target_list.size(); j++) {
//            String name = appList.get(i).get("app_name");
                String pName = appList.get(i).get("app_package");
                if(pName.equals(target_list.get(j))){
                    String name = appList.get(i).get("app_name");
                    String appIconPath = path + pName + ".jpg";
                    Drawable icon = Drawable.createFromPath(appIconPath);
                    mTAdapter.addItem(icon,name,pName);
                }
            }
        }


            btn_select.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ClientAppsViewer.class);
                    startActivity(intent);
                }
            });
            return rootView;
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
                    convertView = inflater.inflate(R.layout.activity_targeted_app_list_item,null);


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
    }


    //////////////////////////////////////
}
