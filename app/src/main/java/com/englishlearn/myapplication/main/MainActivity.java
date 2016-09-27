package com.englishlearn.myapplication.main;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextPaint;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.home.HomeFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");

        Button basis = (Button) findViewById(R.id.basis);
        Button advance = (Button) findViewById(R.id.advance);
        Button advanced = (Button) findViewById(R.id.advanced);

        advanced.setTextSize(15);
        TextPaint tp = advanced.getPaint();
        tp.setFakeBoldText(true);

        basis.setOnClickListener(this);
        advance.setOnClickListener(this);
        advanced.setOnClickListener(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        View login_layout = headerView.findViewById(R.id.login_layout);
        login_layout.setOnClickListener(this);

        selectItem(NavigationActivitysEnum.LEARN);
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
            selectItem(NavigationActivitysEnum.LEARN);
            Toast.makeText(this,"nav_camera",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_gallery) {
            Toast.makeText(this,"nav_gallery",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_slideshow) {
            Toast.makeText(this,"nav_slideshow",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_manage) {
            Toast.makeText(this,"nav_manage",Toast.LENGTH_SHORT).show();
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

        Fragment fragment = null;
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
        setTitle(navigationActivitysEnum);
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
            case R.id.basis:

                break;
            case R.id.advance:

                break;
            case R.id.advanced:

                break;
            default:
                break;
        }
    }
}
