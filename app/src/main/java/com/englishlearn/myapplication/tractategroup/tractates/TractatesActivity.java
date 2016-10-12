package com.englishlearn.myapplication.tractategroup.tractates;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.data.TractateGroup;
import com.englishlearn.myapplication.data.TractateType;

public class TractatesActivity extends AppCompatActivity {

    public static final String TRACTATETYPE = "TractateType";
    public static final String TRACTATEGROUP = "TractateGroup";
    private static final String TAG = TractatesActivity.class.getSimpleName();
    private TractateType tractateType;
    private TractateGroup tractateGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tractates_act);

        if (getIntent().hasExtra(TRACTATETYPE)) {
            tractateType = (TractateType) getIntent().getSerializableExtra(TRACTATETYPE);
        }
        if (getIntent().hasExtra(TRACTATEGROUP)) {
            tractateGroup = (TractateGroup) getIntent().getSerializableExtra(TRACTATEGROUP);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        //返回按钮
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        //标题
        ab.setTitle(R.string.title_tractates);
        Bundle bundle = new Bundle();
        if(tractateType != null){
            bundle.putSerializable(TractatesFragment.TRACTATETYPE,tractateType);
        }
        if(tractateGroup != null){
            bundle.putSerializable(TractatesFragment.TRACTATEGROUP,tractateGroup);
        }
        Fragment fragment = TractatesFragment.newInstance();
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

