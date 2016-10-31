package com.englishlearn.myapplication.tractategroup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.englishlearn.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class TractateGroupsActivity extends AppCompatActivity {

    public static final String OBJECT = "object";
    private static final String TAG = TractateGroupsActivity.class.getSimpleName();
    String[] titles;
    private Object object;
    private List<Fragment> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tractategroups_act);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        //返回按钮
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        //标题
        ab.setTitle(R.string.title_tractategroups);

        titles = getResources().getStringArray(R.array.tractategroupsactivity1_tablayout);
        //初始Fragment
        list = new ArrayList<>();
        list.add(TractateTypesFragment.newInstance());
        list.add(MyTractateGroupsFragment.newInstance());


        //ViewPager
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        android.support.design.widget.TabLayout tableLayout = (android.support.design.widget.TabLayout) findViewById(R.id.tabLayout);
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager()));
        tableLayout.setupWithViewPager(viewPager);
        tableLayout.getTabTextColors();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}


