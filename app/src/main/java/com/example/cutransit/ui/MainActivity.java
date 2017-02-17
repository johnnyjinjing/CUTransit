package com.example.cutransit.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.cutransit.R;
import com.example.cutransit.data.DBHelper;
import com.example.cutransit.util.DataUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AllStopsFragment.Callback,
        FavoriteStopsFragment.Callback {

    private final static String LOG_TAG = MainActivity.class.getSimpleName();

    private ViewPager mPager;
    FragmentAdapter mAdapter;


    static final String INTENT_EXTRA_STOP_ID = "id";
    static final String INTENT_EXTRA_STOP_NAME = "name";
    static final String INTENT_EXTRA_STOP_FAVORITE = "favorite";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setElevation(0);

        mAdapter = new FragmentAdapter(getSupportFragmentManager());
        mAdapter.addFragment(new AllStopsFragment(), "All Stops");
        mAdapter.addFragment(new FavoriteStopsFragment(), "Favorites");
        mAdapter.addFragment(new NearbyStopsFragment(), "Nearby");
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        if (!checkDataBase(this, DBHelper.DATABASE_NAME)) {
            DataUtils.fetchStopsData(this);
        }
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mPager);
    }

    @Override
    public void onItemSelected(String id, String name, int favorite) {
        Intent intent = new Intent(this, StopDepartureActivity.class)
                .putExtra(INTENT_EXTRA_STOP_ID, id)
                .putExtra(INTENT_EXTRA_STOP_NAME, name)
                .putExtra(INTENT_EXTRA_STOP_FAVORITE, favorite);
        startActivity(intent);
    }

    public static class FragmentAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();


        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private boolean checkDataBase(Context context, String db) {
        File dbFile = context.getDatabasePath(db);
        return dbFile.exists();
    }
}
