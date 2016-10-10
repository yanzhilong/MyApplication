package com.englishlearn.myapplication.sentence;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.util.ActivityUtils;


public class SentencesActivity extends AppCompatActivity {

    private static final String TAG = SentencesActivity.class.getSimpleName();
    private SentencesContract.Presenter presenter;
    private SentencesSelectContract.Presenter selectPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sentences_act);

        Log.d(TAG,"onCreate");
        handleIntent(getIntent());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle(R.string.sentences_title);

        SentencesFragment cleanFragment = (SentencesFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (cleanFragment == null) {
            cleanFragment = SentencesFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), cleanFragment, R.id.contentFrame);
        }
        presenter = new SentencesPresenter( cleanFragment);
        selectPresenter = new SentencesSelectPresenter(cleanFragment);
    }

    @Override
    protected void onNewIntent(Intent intent) {

        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        Log.d(TAG,"handleIntent" + intent.getAction());
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            presenter.setQuery(query);
            Log.d(TAG,"query:" + query);
            //通过某种方法，根据请求检索你的数据
        }
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
