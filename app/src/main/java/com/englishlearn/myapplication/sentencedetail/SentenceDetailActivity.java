package com.englishlearn.myapplication.sentencedetail;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.util.ActivityUtils;


public class SentenceDetailActivity extends AppCompatActivity {

    public static final String SENTENCE_ID = "sentence_id";
    public static final String ID = "id";

    private SentenceDetailContract.Presenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sentencedetail_act);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        //返回按钮
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        ab.setTitle(R.string.sentencedetail_title);

        String sentenceid = getIntent().getStringExtra(SENTENCE_ID);
        String id = getIntent().getStringExtra(ID);

        SentenceDetailFragment fragment = (SentenceDetailFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (fragment == null) {
            fragment = SentenceDetailFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.contentFrame);
        }
        presenter = new SentenceDetailPresenter(fragment,id,sentenceid);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
