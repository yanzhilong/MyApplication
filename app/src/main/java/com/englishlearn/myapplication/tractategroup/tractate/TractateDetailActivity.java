package com.englishlearn.myapplication.tractategroup.tractate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.data.Tractate;

public class TractateDetailActivity extends AppCompatActivity {

    public static final String TRACTATE = "tractate";
    public static final String PREVIEW = "preview";//是否是预览
    private static final String TAG = TractateDetailActivity.class.getSimpleName();
    private Tractate tractate;
    private boolean preview;//是否是预览

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tractatedetail_act);

        if (getIntent().hasExtra(TRACTATE)) {
            tractate = (Tractate) getIntent().getSerializableExtra(TRACTATE);
            preview = getIntent().getBooleanExtra(PREVIEW,false);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        //返回按钮
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        //标题
        ab.setTitle(R.string.title_tractatedetail);

        Fragment fragment = TractateDetailFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TractateDetailFragment.TRACTATE,tractate);
        fragment.setArguments(bundle);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentFrame, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

