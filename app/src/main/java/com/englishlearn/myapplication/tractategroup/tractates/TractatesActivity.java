package com.englishlearn.myapplication.tractategroup.tractates;

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

/**
 * 文章分类进来，显示
 * 当前分类的热门文章
 * 热门文章分组
 * 我的文章分组，里面是我的文章
 * 我收藏的文章
 * 我收藏的文章分组
 */
public class TractatesActivity extends AppCompatActivity {

    public static final String OBJECT = "object";
    public static final String TRACTATETYPE = "TractateType";
    private static final String TAG = TractatesActivity.class.getSimpleName();
    String[] titles;
    private Object object;
    private List<Fragment> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tractates_act);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        //返回按钮
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        //标题
        ab.setTitle(R.string.title_tractates);

        titles = getResources().getStringArray(R.array.tractategroupsactivity_tablayout);
        //初始Fragment
        list = new ArrayList<>();
        list.add(TractateTopFragment.newInstance());
        list.add(TractatrGroupTopFragment.newInstance());
        list.add(MyCreateTractateGroupFragment.newInstance());
        list.add(MyCollectTractateFragment.newInstance());
        list.add(MyCollectTractateGroupFragment.newInstance());

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


