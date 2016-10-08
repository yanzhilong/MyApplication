package com.englishlearn.myapplication.grammardetail;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.data.Grammar;

public class GrammarDetailActivity extends AppCompatActivity {

    public static final String OBJECT = "object";
    private static final String TAG = GrammarDetailActivity.class.getSimpleName();
    private Grammar grammar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grammardetail_act);

        if (getIntent().hasExtra(OBJECT)) {
            grammar = (Grammar) getIntent().getSerializableExtra(OBJECT);
        }

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String cheeseName = getResources().getString(R.string.title_grammardetail);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(cheeseName);

        loadBackdrop();

        TextView name = (TextView) findViewById(R.id.name);
        TextView content = (TextView) findViewById(R.id.content);

        if(grammar != null){
            name.setText(grammar.getTitle());
            content.setText(grammar.getContent());
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



