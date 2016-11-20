package com.englishlearn.myapplication.sentencegroups.sentences;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.englishlearn.myapplication.R;

public class SentencesActivity extends AppCompatActivity {

    public static final String SENTENCEGROUP = "sentencegroup";
    public static final String SENTENCECOLLECTGROUP = "sentencecollectgroup";
    public static final String SENTENCEGROUPCOLLECT = "sentencegroupcollect";//收藏的单词分组
    public static final String TYPE = "grouptype";

    private static final String TAG = SentencesActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sentences_act);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        //返回按钮
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        //标题
        ab.setTitle(R.string.title_sentences);

        Fragment fragment = SentencesFragment.newInstance();

        Bundle bundle = getIntent().getExtras();
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

