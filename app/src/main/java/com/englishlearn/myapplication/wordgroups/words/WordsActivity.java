package com.englishlearn.myapplication.wordgroups.words;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.data.WordGroup;

public class WordsActivity extends AppCompatActivity {

    public static final String OBJECT = "object";
    private static final String TAG = WordsActivity.class.getSimpleName();
    private WordGroup wordGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.words_act);

        if (getIntent().hasExtra(OBJECT)) {
            wordGroup = (WordGroup) getIntent().getSerializableExtra(OBJECT);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        //返回按钮
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        //标题
        ab.setTitle(R.string.title_words);

        Fragment fragment = WordsFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putSerializable(WordsActivity.OBJECT,wordGroup);
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

