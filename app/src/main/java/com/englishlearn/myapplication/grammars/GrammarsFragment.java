package com.englishlearn.myapplication.grammars;

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

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.data.Grammar;
import com.englishlearn.myapplication.data.Sentence;

import java.util.List;

/**
 * Created by yanzl on 16-7-20.
 */
public class GrammarsFragment extends Fragment implements GrammarsContract.View {

    private static final String TAG = GrammarsFragment.class.getSimpleName();

    private GrammarsContract.Presenter mPresenter;
    public static GrammarsFragment newInstance() {
        return new GrammarsFragment();
    }

    @Override
    public void setPresenter(GrammarsContract.Presenter presenter) {
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
        View root = inflater.inflate(R.layout.sentences_frag, container, false);

        //如果有设置菜单，需要加这个
        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getGrammars();
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
    public void showGrammars(List<Grammar> grammars) {
        Log.d(TAG,"showGrammars" + grammars.size());
    }

    @Override
    public void emptyGrammars() {
        Log.d(TAG,"emptyGrammars");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }
}
