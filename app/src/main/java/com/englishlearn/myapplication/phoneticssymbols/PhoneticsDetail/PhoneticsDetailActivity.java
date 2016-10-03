package com.englishlearn.myapplication.phoneticssymbols.PhoneticsDetail;

import android.os.Bundle;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.englishlearn.myapplication.data.PhoneticsSymbols;
import com.englishlearn.myapplication.util.ActivityUtils;

import com.englishlearn.myapplication.R;

public class PhoneticsDetailActivity extends AppCompatActivity {

    private PhoneticsDetailContract.Presenter presenter;
    public static final String PHONETICS = "PHONETICS";
    private PhoneticsSymbols phoneticsSymbols;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phoneticsdetail_act);

        if(getIntent().hasExtra(PHONETICS)){
            phoneticsSymbols = (PhoneticsSymbols) getIntent().getSerializableExtra(PHONETICS);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        //返回按钮
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        //标题
        ab.setTitle(R.string.title_phoneticsdetail);

        PhoneticsDetailFragment fragment = (PhoneticsDetailFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        Bundle bundle = new Bundle();
        bundle.putSerializable(PHONETICS,phoneticsSymbols);
        if (fragment == null) {
            fragment = PhoneticsDetailFragment.newInstance();
            fragment.setArguments(bundle);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.contentFrame);
        }
        presenter = new PhoneticsDetailPresenter(fragment,phoneticsSymbols);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
