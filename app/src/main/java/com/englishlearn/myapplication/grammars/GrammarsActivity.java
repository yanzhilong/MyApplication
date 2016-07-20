package com.englishlearn.myapplication.grammars;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.util.ActivityUtils;


public class GrammarsActivity extends AppCompatActivity {

    private GrammarsContract.Presenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grammars_act);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.grammar_title);

        GrammarsFragment cleanFragment = (GrammarsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (cleanFragment == null) {
            cleanFragment = GrammarsFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), cleanFragment, R.id.contentFrame);
        }
        presenter = new GrammarsPresenter( cleanFragment);
    }
}
