package com.englishlearn.myapplication.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.grammars.GrammarsActivity;
import com.englishlearn.myapplication.loginuser.LoginUserActivity;
import com.englishlearn.myapplication.registeruser.RegisterUserActivity;
import com.englishlearn.myapplication.search.SearchActivity;
import com.englishlearn.myapplication.searchsentences.SearchSentencesActivity;
import com.englishlearn.myapplication.sentences.SentencesActivity;
import com.englishlearn.myapplication.updateuser.UpdateUserActivity;

/**
 * Created by yanzl on 16-7-20.
 */
public class MainFragment extends Fragment implements MainContract.View, View.OnClickListener {

    private static final String TAG = MainFragment.class.getSimpleName();
    private MainContract.Presenter mPresenter;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.main_frag, container, false);

        Button sentence = (Button) root.findViewById(R.id.sentences);
        Button grammar = (Button) root.findViewById(R.id.grammars);
        Button search = (Button) root.findViewById(R.id.search);
        Button searchsentences = (Button) root.findViewById(R.id.searchsentences);
        Button registeruser = (Button) root.findViewById(R.id.registeruser);
        Button loginuser = (Button) root.findViewById(R.id.loginuser);
        Button updateuser = (Button) root.findViewById(R.id.updateuser);


        sentence.setOnClickListener(this);
        grammar.setOnClickListener(this);
        search.setOnClickListener(this);
        searchsentences.setOnClickListener(this);
        registeruser.setOnClickListener(this);
        loginuser.setOnClickListener(this);
        updateuser.setOnClickListener(this);

        //如果有设置菜单，需要加这个
        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_base, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "点击菜单Item");
        switch (item.getItemId()) {
            case R.id.menu_base:
                Log.d(TAG, "点击菜单项");
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sentences:
                Intent sentenceintent = new Intent(this.getContext(),SentencesActivity.class);
                this.startActivity(sentenceintent);
                break;
            case R.id.grammars:
                Intent grammarintent = new Intent(this.getContext(),GrammarsActivity.class);
                this.startActivity(grammarintent);
                break;
            case R.id.search:
                Intent searchintent = new Intent(this.getContext(),SearchActivity.class);
                this.startActivity(searchintent);
                break;
            case R.id.searchsentences:
                Intent searchsentencesintent = new Intent(this.getContext(),SearchSentencesActivity.class);
                this.startActivity(searchsentencesintent);
                break;
            case R.id.registeruser:
                Intent registeruserintent = new Intent(this.getContext(),RegisterUserActivity.class);
                this.startActivity(registeruserintent);
                break;
            case R.id.loginuser:
                Intent loginuserintent = new Intent(this.getContext(),LoginUserActivity.class);
                this.startActivity(loginuserintent);
                break;
            case R.id.updateuser:
                Intent updateuserintent = new Intent(this.getContext(),UpdateUserActivity.class);
                this.startActivity(updateuserintent);
                break;
            default:
                break;
        }
    }
}
