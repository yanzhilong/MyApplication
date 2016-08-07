package com.englishlearn.myapplication.grammars;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.sentences.SentencesSelectPresenter;
import com.englishlearn.myapplication.util.ActivityUtils;


public class GrammarsActivity extends AppCompatActivity {

    private GrammarsContract.Presenter presenter;
    private GrammarsSelectContract.Presenter selectPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grammars_act);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle(R.string.grammar_title);

        GrammarsFragment cleanFragment = (GrammarsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (cleanFragment == null) {
            cleanFragment = GrammarsFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), cleanFragment, R.id.contentFrame);
        }
        presenter = new GrammarsPresenter( cleanFragment);
        selectPresenter = new GrammarsSelectPresenter(cleanFragment);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                //mDrawerLayout.openDrawer(GravityCompat.START);
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
