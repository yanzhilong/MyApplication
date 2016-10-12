package com.englishlearn.myapplication.tractategroup.tractates.tractate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.data.Tractate;

public class TractateDetailActivity extends AppCompatActivity {

    public static final String OBJECT = "object";
    private static final String TAG = TractateDetailActivity.class.getSimpleName();
    private Tractate tractate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tractatedetail_act);

        if (getIntent().hasExtra(OBJECT)) {
            tractate = (Tractate) getIntent().getSerializableExtra(OBJECT);
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
        bundle.putSerializable(TractateDetailFragment.OBJECT,tractate);
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

