package com.englishlearn.myapplication.search;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.provide.SuggestionsProvider;


/**
 * Created by yanzl on 16-7-20.
 */
public class SearchFragment extends Fragment implements SearchContract.View, SearchView.OnQueryTextListener, View.OnClickListener {

    private static final String TAG = SearchFragment.class.getSimpleName();
    private SearchView mSearchView;
    private SearchRecentSuggestions suggestions;//保存搜索历史
    private Button search_sentences;//搜索句子
    private Button search_grammars;//搜索语法

    private SearchContract.Presenter mPresenter;
    private MenuItem searchItem;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void setPresenter(SearchContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        suggestions = new SearchRecentSuggestions(this.getContext(), SuggestionsProvider.AUTHORITY, SuggestionsProvider.MODE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.serach_frag, container, false);

        search_sentences = (Button) root.findViewById(R.id.search_sentences);
        search_grammars = (Button) root.findViewById(R.id.search_grammars);

        search_sentences.setOnClickListener(this);
        search_grammars.setOnClickListener(this);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_frag_menu, menu);

        searchItem = menu.findItem(R.id.search);
        // 关联检索配置和SearchView
        SearchManager searchManager =
                (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setSearchableInfo(
                searchManager.getSearchableInfo(getActivity().getComponentName()));

        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setIconifiedByDefault(true);
        mSearchView.setMaxWidth(1000);

        //展开
        MenuItemCompat.expandActionView(searchItem);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d(TAG, "onQueryTextSubmit = " + query);
        suggestions.saveRecentQuery(query, "query");
        if (mSearchView != null) {
            // 得到输入管理对象
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                // 这将让键盘在所有的情况下都被隐藏，但是一般我们在点击搜索按钮后，输入法都会乖乖的自动隐藏的。
                imm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0); // 输入法如果是显示状态，那么就隐藏输入法
            }
            mSearchView.clearFocus(); // 不获取焦点
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        Log.d(TAG,"onQueryTextChange:" + newText);
        mPresenter.filterSentences(newText);
        return false;
    }

    @Override
    public void setQuery(final String query) {
        mSearchView.setQuery(query,false);
        mSearchView.clearFocus();
        mSearchView.post(new Runnable() {
            @Override
            public void run() {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_sentences:
                break;
            case R.id.search_grammars:
                break;
        }
    }
}
