package com.englishlearn.myapplication.clipblard;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.englishlearn.myapplication.R;


public class ClipboardActivity extends AppCompatActivity {

    public static final String CLIPBOARDVALUE = "clipboardvalue";
    private static final String TAG = ClipboardActivity.class.getSimpleName();
    private String clipboadvalue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clipboard_act);

        if (getIntent().hasExtra(CLIPBOARDVALUE)) {
            clipboadvalue = getIntent().getStringExtra(CLIPBOARDVALUE);
            Log.d(TAG,"数据" + clipboadvalue);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        //返回按钮
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        //标题
        ab.setTitle(R.string.title_clipboard);

        /*Fragment fragment = null;

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentFrame, fragment)
                    .commit();
        }*/
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

