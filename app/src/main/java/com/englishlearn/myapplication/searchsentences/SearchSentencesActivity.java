package com.englishlearn.myapplication.searchsentences;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.util.ActivityUtils;


public class SearchSentencesActivity extends AppCompatActivity {

    private static final String TAG = SearchSentencesActivity.class.getSimpleName();
    private SearchSentencesContract.Presenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchsentences_act);

        Log.d(TAG,"onCreate");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle(R.string.sentences_title);

        SearchSentencesFragment cleanFragment = (SearchSentencesFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (cleanFragment == null) {
            cleanFragment = SearchSentencesFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), cleanFragment, R.id.contentFrame);
        }
        presenter = new SearchSentencesPresenter( cleanFragment);
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
