package com.englishlearn.myapplication.searchresults;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.englishlearn.myapplication.R;


/**
 * Created by yanzl on 16-7-20.
 */
public class SearchResultsFragment extends Fragment implements SearchResultsContract.View {

    private static final String TAG = SearchResultsFragment.class.getSimpleName();

    private SearchResultsContract.Presenter mPresenter;
    public static SearchResultsFragment newInstance() {
        return new SearchResultsFragment();
    }

    @Override
    public void setPresenter(SearchResultsContract.Presenter presenter) {
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
        View root = inflater.inflate(R.layout.sentencedetail_frag, container, false);

        // Set up floating action button
        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_edit_sentence);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        FloatingActionButton fabdel =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_delete_sentence);

        fabdel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

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
