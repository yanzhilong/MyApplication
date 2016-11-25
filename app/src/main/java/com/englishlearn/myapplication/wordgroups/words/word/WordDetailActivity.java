package com.englishlearn.myapplication.wordgroups.words.word;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.englishlearn.myapplication.R;

public class WordDetailActivity extends AppCompatActivity {

    public static final String WORDNAME = "wordname";
    private static final String TAG = WordDetailActivity.class.getSimpleName();
    private String wordname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.worddetail_act);

        if (getIntent().hasExtra(WORDNAME)) {
            wordname = getIntent().getStringExtra(WORDNAME);
        }

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String cheeseName = wordname!= null ? wordname : getResources().getString(R.string.title_worddetail);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(cheeseName);

        loadBackdrop();

	Fragment fragment = WordDetailFragment.newInstance();
        Bundle arguments = new Bundle();
        arguments.putSerializable(WORDNAME,wordname);
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



