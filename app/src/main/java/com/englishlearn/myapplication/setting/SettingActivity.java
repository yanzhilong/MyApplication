package com.englishlearn.myapplication.setting;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.englishlearn.myapplication.R;


public class SettingActivity extends AppCompatActivity {

    public static final String OBJECT = "object";
    private static final String TAG = SettingActivity.class.getSimpleName();
    private Object object;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_act);

        if (getIntent().hasExtra(OBJECT)) {
            object = getIntent().getSerializableExtra(OBJECT);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        //返回按钮
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        //标题
        ab.setTitle(R.string.title_setting);

        SettingFragment settingFragment = SettingFragment.newInstance();

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.contentFrame, settingFragment)
                    .commit();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

