package com.englishlearn.myapplication.addeditgrammar;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.util.ActivityUtils;


public class AddEditGrammarActivity extends AppCompatActivity {

    public static final String ID = "id";
    private AddEditGrammarContract.Presenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addeditgrammar_act);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        String id = null;

        AddEditGrammarFragment addEditSentenceFragment = (AddEditGrammarFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (addEditSentenceFragment == null) {
            addEditSentenceFragment = AddEditGrammarFragment.newInstance();

            if(getIntent().hasExtra(ID)){
                id = getIntent().getStringExtra(ID);
                actionBar.setTitle(R.string.editgrammar_title);
            }else {
                actionBar.setTitle(R.string.addgrammar_title);
            }

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), addEditSentenceFragment, R.id.contentFrame);
        }
        presenter = new AddEditGrammarPresenter(addEditSentenceFragment,id);
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
