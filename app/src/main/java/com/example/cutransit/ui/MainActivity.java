package com.example.cutransit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.cutransit.R;
import com.example.cutransit.util.DataUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AllStopsFragment.Callback {

    private ViewPager mPager;
    FragmentAdapter mAdapter;


    static final String INTENT_EXTRA_STOP_ID = "id";
    static final String INTENT_EXTRA_STOP_NAME = "name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setElevation(0);

        mAdapter = new FragmentAdapter(getSupportFragmentManager());
        mAdapter.addFragment(new AllStopsFragment(), "All Stops");
        mAdapter.addFragment(new AllStopsFragment(), "Favorites");
        mAdapter.addFragment(new AllStopsFragment(), "Nearby");
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mPager);
    }

    public void fetchStopsData(View view) {
        DataUtils.fetchStopsData(this);
    }


    @Override
    public void onItemSelected(String id, String name) {
        Intent intent = new Intent(this, StopDepartureActivity.class)
                .putExtra(INTENT_EXTRA_STOP_ID, id)
                .putExtra(INTENT_EXTRA_STOP_NAME, name);
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
}
