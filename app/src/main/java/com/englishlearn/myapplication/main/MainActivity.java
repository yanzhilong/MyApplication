package com.englishlearn.myapplication.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.englishlearn.myapplication.MyApplication;
import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.advanced.AdvancedFragment;
import com.englishlearn.myapplication.data.User;
import com.englishlearn.myapplication.data.source.Repository;
import com.englishlearn.myapplication.dict.DictActivity;
import com.englishlearn.myapplication.elementary.ElementaryFragment;
import com.englishlearn.myapplication.intermediate.IntermediateFragment;
import com.englishlearn.myapplication.setting.SettingActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    //String[] titles = new String[]{getResources().getString(R.string.elementary),getResources().getString(R.string.intermediate),getResources().getString(R.string.advanced)};

    String[] titles;
    private List<Fragment> list;

    @Inject
    Repository repository;
    private TextView loginname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyApplication.instance.getAppComponent().inject(this);

        titles = getResources().getStringArray(R.array.main_tablayout);
        //初始Fragment
        list = new ArrayList<>();
        list.add(ElementaryFragment.newInstance());
        list.add(IntermediateFragment.newInstance());
        list.add(AdvancedFragment.newInstance());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");

        //初始Navigation
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //设置Navigation 的 Item监听　
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //设置登陆的按钮监听
        View headerView = navigationView.getHeaderView(0);
        View login_layout = headerView.findViewById(R.id.login_layout);
        loginname = (TextView) headerView.findViewById(R.id.loginname);

        login_layout.setOnClickListener(this);


        //selectItem(NavigationActivitysEnum.LEARN);

        //ViewPager
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        android.support.design.widget.TabLayout tableLayout = (android.support.design.widget.TabLayout) findViewById(R.id.tabLayout);
        viewPager.setAdapter(new MainFragmentPagerAdapter(this.getSupportFragmentManager()));
        tableLayout.setupWithViewPager(viewPager);
        tableLayout.getTabTextColors();

        //检查用户
        checkoutLogin();
    }

    /**
     * 检测用户登陆信息
     */
    private void checkoutLogin(){

        User user = repository.getUserInfo();
        if(user != null){
            loginname.setText(user.getUsername());
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            //selectItem(NavigationActivitysEnum.LEARN);
            Toast.makeText(this,"nav_camera",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_gallery) {
            Toast.makeText(this,"nav_gallery",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_slideshow) {
            Toast.makeText(this,"nav_slideshow",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_manage) {
            Toast.makeText(this,"nav_manage",Toast.LENGTH_SHORT).show();
        }else if (id == R.id.nav_setting) {
            item.setChecked(false);
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_dict) {
            item.setChecked(false);
            Intent intent = new Intent(this, DictActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            Toast.makeText(this,"nav_share",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_send) {
            Toast.makeText(this,"nav_send",Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void selectItem(NavigationActivitysEnum navigationActivitysEnum){

        /*Fragment fragment = null;
        switch (navigationActivitysEnum){
            case LEARN:
                fragment = NavigationActivitysEnum.LEARN.getmFragment();
                break;
            default:
                break;
        }
        if(fragment == null)
            return;
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, HomeFragment.newInstance());
        ft.commit();

        // update selected item title, then close the drawer
        setTitle(navigationActivitysEnum);*/
    }

    private void changerLearnTitle(){
        
    }

    public void setTitle(NavigationActivitysEnum navigationActivitysEnum){

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_layout:
                //登陆或详情
                Toast.makeText(this,"login/register",Toast.LENGTH_SHORT).show();
                break;
            /*case R.id.basis:

                break;
            case R.id.advance:

                break;
            case R.id.advanced:

                break;*/
            default:
                break;
        }
    }

    //
    private class MainFragmentPagerAdapter extends FragmentPagerAdapter{

        public MainFragmentPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
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
