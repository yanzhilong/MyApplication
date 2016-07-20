package com.englishlearn.myapplication.main;

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


        sentence.setOnClickListener(this);
        grammar.setOnClickListener(this);

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

                break;
            case R.id.grammars:

                break;
            default:
                break;
        }
    }
}
