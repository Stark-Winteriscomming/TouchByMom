package com.example.changho.kidmanagement;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.example.changho.kidmanagement.service.DbService;
import com.example.changho.kidmanagement.service.ManagementService;
import com.example.changho.kidmanagement.service.ManagementServiceImpl;
import com.example.changho.kidmanagement.service.MySQLiteOpenHelper;
import com.example.changho.kidmanagement.utils.SharedData;

public class MainActivity extends AppCompatActivity {
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    Context context = this;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    // TODO: 2016-10-31
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        //testing
        DbService dbService = new DbService(this);
        //dbService.deleteAll();
        //dbService.selectAll();


//        dbService.insert("app1","com.app1");
        //dbService.selectAll();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //서버 앱 정보
            ManagementService managementService = new ManagementServiceImpl(this);
            managementService.getAppInfo();
            return true;
        }
        /**
         * 서버정보 가져오기
         */
        else if(id == R.id.action_sync){
            ManagementService managementService = new ManagementServiceImpl(this);
//             서버 이미지 파일
            managementService.synkChildAppState();

        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
//            View rootView;
//            int fragNum = getArguments().getInt(ARG_SECTION_NUMBER);
//
//            if(fragNum == 1){
//                rootView = inflater.inflate(R.layout.activity_client_app_show, container, false);
//                return rootView;
//            }else {
//                rootView = inflater.inflate(R.layout.activity_client_app_run_time, container, false);
//                return rootView;
//            }
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }
    //

    //

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            //return PlaceholderFragment.newInstance(position + 1);

            //
            Fragment fragment = null;
            Bundle args = null;
            switch (position) {
                case 0:
                    fragment = new ClientAppShow();
                    args = new Bundle();
                    break;
                case 1:
                    fragment = new TargetTimeWriter();
                    args = new Bundle();
                    break;
                case 2:
                    fragment = new ClientAppShow();
                    args = new Bundle();
            }
            return fragment;
            //
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "잠금 앱 지정";
                case 1:
                    return "잠금 앱 시간부여";
                case 2:
                    return "아이의 앱 사용패턴";
            }
            return null;
        }
    }
}
