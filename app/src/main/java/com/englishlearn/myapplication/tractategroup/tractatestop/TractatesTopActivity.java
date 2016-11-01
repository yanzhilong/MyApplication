package com.englishlearn.myapplication.tractategroup.tractatestop;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.data.TractateType;

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
public class TractatesTopActivity extends AppCompatActivity {

    public static final String TRACTATETYPE = "TractateType";
    private static final String TAG = TractatesTopActivity.class.getSimpleName();
    String[] titles;
    private List<Fragment> list;
    private TractateType tractateType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tractatestop_act);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        //返回按钮
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        //标题
        ab.setTitle(R.string.title_tractates);

        if (getIntent().hasExtra(TRACTATETYPE)) {
            tractateType = (TractateType) getIntent().getSerializableExtra(TRACTATETYPE);
        }

        Bundle bundle = new Bundle();
        if(tractateType != null){
            bundle.putSerializable(TractateTopFragment.TRACTATETYPE,tractateType);
        }
        Fragment fragment = TractateTopFragment.newInstance();
        fragment.setArguments(bundle);

        Fragment fragment1 = TractatrGroupTopFragment.newInstance();
        fragment1.setArguments(bundle);

        titles = getResources().getStringArray(R.array.tractatestopactivity_tablayout);
        //初始Fragment
        list = new ArrayList<>();
        list.add(fragment);
        list.add(fragment1);

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


