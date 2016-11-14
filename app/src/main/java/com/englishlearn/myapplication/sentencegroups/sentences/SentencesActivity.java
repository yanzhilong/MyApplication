package com.englishlearn.myapplication.sentencegroups.sentences;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.data.SentenceCollectGroup;
import com.englishlearn.myapplication.data.SentenceGroup;

public class SentencesActivity extends AppCompatActivity {

    public static final String OBJECT = "object";
    public static final String TYPE = "sentencegrouptype";
    private static final String TAG = SentencesActivity.class.getSimpleName();
    private SentenceGroup sentenceGroup;
    private SentenceCollectGroup sentenceCollectGroup;//句子收藏分组
    private SentenceGroupType sentenceGroupType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sentences_act);

        if (getIntent().hasExtra(OBJECT)) {
            sentenceGroup = (SentenceGroup) getIntent().getSerializableExtra(OBJECT);
            sentenceGroupType = (SentenceGroupType) getIntent().getSerializableExtra(TYPE);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        //返回按钮
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        //标题
        ab.setTitle(R.string.title_sentences);

        Fragment fragment = SentencesFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putSerializable(SentencesFragment.OBJECT,sentenceGroup);
        bundle.putSerializable(SentencesFragment.TYPE,sentenceGroupType);
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

