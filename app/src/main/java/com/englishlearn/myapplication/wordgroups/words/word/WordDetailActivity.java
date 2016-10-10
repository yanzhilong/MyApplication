package com.englishlearn.myapplication.wordgroups.words.word;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.data.Word;

public class WordDetailActivity extends AppCompatActivity {

    public static final String OBJECT = "object";
    private static final String TAG = WordDetailActivity.class.getSimpleName();
    private Word word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.worddetail_act);

        if (getIntent().hasExtra(OBJECT)) {
            word = (Word) getIntent().getSerializableExtra(OBJECT);
        }

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String cheeseName = word!= null ? word.getName() : getResources().getString(R.string.title_worddetail);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(cheeseName);

        loadBackdrop();

	Fragment fragment = WordDetailFragment.newInstance();
        Bundle arguments = new Bundle();
        arguments.putSerializable(WordDetailFragment.OBJECT,word);
        fragment.setArguments(arguments);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentFrame, fragment)
                    .commit();
        }

    }

    private void loadBackdrop() {
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        Glide.with(this).load(R.drawable.cheese_2).centerCrop().into(imageView);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}



