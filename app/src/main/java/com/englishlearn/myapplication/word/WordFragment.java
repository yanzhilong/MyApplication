package com.englishlearn.myapplication.word;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.englishlearn.myapplication.R;


/**
 * Created by yanzl on 16-7-20.
 */
public class WordFragment extends Fragment implements WordContract.View {

    private static final String TAG = WordFragment.class.getSimpleName();

    private WordContract.Presenter mPresenter;
    public static WordFragment newInstance() {
        return new WordFragment();
    }

    @Override
    public void setPresenter(WordContract.Presenter presenter) {
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
        View root = inflater.inflate(R.layout.wordgroups_frag, container, false);

        //如果有设置菜单，需要加这个
        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }
}
