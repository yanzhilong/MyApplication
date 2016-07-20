package com.englishlearn.myapplication.sentences;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.util.ActivityUtils;


public class SentencesActivity extends AppCompatActivity {

    private SentencesContract.Presenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sentences_act);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.sentences_title);

        SentencesFragment cleanFragment = (SentencesFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (cleanFragment == null) {
            cleanFragment = SentencesFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), cleanFragment, R.id.contentFrame);
        }
        presenter = new SentencesPresenter( cleanFragment);
    }
}
