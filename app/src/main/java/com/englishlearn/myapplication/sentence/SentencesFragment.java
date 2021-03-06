package com.englishlearn.myapplication.sentence;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.addeditsentence.AddEditSentenceActivity;
import com.englishlearn.myapplication.data.Sentence;
import com.englishlearn.myapplication.provide.SuggestionsProvider;
import com.englishlearn.myapplication.sentencedetail1.SentenceDetail1Activity;
import com.englishlearn.myapplication.ui.LoadMoreListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanzl on 16-7-20.
 */
public class SentencesFragment extends Fragment implements SentencesContract.View,SentencesSelectContract.View, View.OnClickListener, SearchView.OnQueryTextListener {

    private static final String TAG = SentencesFragment.class.getSimpleName();
    private SentencesAdapter sentencesAdapter;
    private SentencesContract.Presenter mPresenter;
    private SentencesSelectContract.Presenter selectPresenter;
    private LoadMoreListView sentences_listview;
    private RelativeLayout sentences_edit_rela;
    private Button deletes;
    private CheckBox allSelect;
    private FloatingActionButton fab;
    private SearchView mSearchView;
    private SearchRecentSuggestions suggestions;
    private SwipeRefreshLayout srl;

    public static SentencesFragment newInstance() {
        return new SentencesFragment();
    }

    @Override
    public void setPresenter(SentencesContract.Presenter presenter) {
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
        View root = inflater.inflate(R.layout.sentences_frag, container, false);

        sentencesAdapter = new SentencesAdapter(selectPresenter);
        sentences_listview = (LoadMoreListView) root.findViewById(R.id.sentences_listview);
        sentences_edit_rela = (RelativeLayout) root.findViewById(R.id.sentences_edit_rela);
        deletes = (Button) root.findViewById(R.id.deletes);
        allSelect = (CheckBox) root.findViewById(R.id.allSelect);
        sentences_listview.setAdapter(sentencesAdapter);
        // Set up floating action button
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_add_sentence);
        fab.setImageResource(R.drawable.ic_add);

        srl = (SwipeRefreshLayout)root.findViewById(R.id.refresh_layout);

        allSelect.setOnClickListener(this);
        deletes.setOnClickListener(this);
        fab.setOnClickListener(this);

        sentences_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position >= sentencesAdapter.getSentences().size()){
                    //底部加戴Item
                    return;
                }else if(selectPresenter.isEdit()){
                    selectPresenter.onClick(sentencesAdapter.getSentences().get(position));
                }else{
                    Sentence sentence = sentencesAdapter.getSentences().get(position);
                    Intent detail = new Intent(SentencesFragment.this.getContext(), SentenceDetail1Activity.class);
                    detail.putExtra(SentenceDetail1Activity.ID,sentence.getObjectId());
                    startActivity(detail);
                }
            }
        });

        sentences_listview.setOnLoadMoreLister(new LoadMoreListView.OnLoadMoreLister() {
            @Override
            public void loadingMore() {
                mPresenter.getSentencesNextPage();
            }
        });
        if(selectPresenter.isEdit()){
            showaddSentence();
        }else{
            hideSentencesEdit();
        }
        mPresenter.getSentences();//获取数据


        final ScrollChildSwipeRefreshLayout swipeRefreshLayout =
                (ScrollChildSwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );
        swipeRefreshLayout.setScrollUpChild(sentences_listview);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.unsubscribe();
                Log.d(TAG, "下拉刷新");
                mPresenter.getSentences();//加载列表
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

    private Menu menu;
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        this.menu = menu;
        Log.d(TAG,"onPrepareOptionsMenu");
        MenuItem edit = menu.findItem(R.id.sentences_edit);
        MenuItem cancel = menu.findItem(R.id.menu_cancel);

        if(selectPresenter.isEdit())
        {
            edit.setVisible(false);
            cancel.setVisible(true);
        }
        else
        {
            edit.setVisible(true);
            cancel.setVisible(false);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.sentences_frag_menu, menu);

        // 关联检索配置和SearchView
        SearchManager searchManager =
                (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) menu.findItem(R.id.search).getActionView();
        mSearchView.setSearchableInfo(
                searchManager.getSearchableInfo(getActivity().getComponentName()));

        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setIconifiedByDefault(true);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setMaxWidth(1000);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sentences_edit:
                Log.d(TAG, "点击编辑项");
                selectPresenter.edit();
                onPrepareOptionsMenu(menu);
                break;
            case R.id.sentences_setting:
                Log.d(TAG, "点击设置项");
                break;
            case R.id.menu_cancel:
                Log.d(TAG, "点击取消项");
                selectPresenter.unedit();
                onPrepareOptionsMenu(menu);
                break;
        }
        return true;
    }

    @Override
    public void showSentences(List<Sentence> sentences) {
        Log.d(TAG,"showSentences" + sentences.size());
        if(mPresenter.hasMore()){
            sentences_listview.setIsEnd(false);
        }else {
            sentences_listview.setIsEnd(true);
        }
        sentencesAdapter.replace(sentences);
        sentences_listview.loadingComplete();
    }

    @Override
    public void showGetSentencesFail() {
        Log.d(TAG,"showGetSentencesFail");
        sentences_listview.loadFail();
    }

    @Override
    public void showaddSentence() {

        Log.d(TAG,"showaddSentence");
        Intent ahowAddSentenctIntent = new Intent(this.getContext(), AddEditSentenceActivity.class);
        startActivity(ahowAddSentenctIntent);
    }

    @Override
    public void setQuery(String query) {
        mSearchView.setQuery(query,false);
        mSearchView.clearFocus();
    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        // Make sure setRefreshing() is called after the layout is done with everything else.
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });
    }

    @Override
    public void showDeleteResult(int success, int fail) {
        selectPresenter.unedit();
        onPrepareOptionsMenu(menu);
        mPresenter.getSentences();
        Snackbar.make(this.getView(),getResources().getString(R.string.delete_result,success,fail), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }

    @Override
    public void notifyDataSetChanged() {
        sentencesAdapter.notifyDataSetChanged();
    }

    public void setPresenter(SentencesSelectContract.Presenter presenter) {
        selectPresenter = presenter;
    }

    @Override
    public void showSentencesEdit() {
        sentences_edit_rela.setVisibility(View.VISIBLE);
        fab.setVisibility(View.GONE);
    }

    @Override
    public void hideSentencesEdit() {
        sentences_edit_rela.setVisibility(View.GONE);
        fab.setVisibility(View.VISIBLE);
    }

    @Override
    public void showEditCount(int count) {
        deletes.setText(getString(R.string.edit_delete) + (count > 0 ? "(" +count+")" : ""));
    }

    @Override
    public List<Sentence> getSentences() {
        return sentencesAdapter.getSentences();
    }

    @Override
    public void showDeleteEnabled(Boolean enabled) {
        deletes.setEnabled(enabled);
    }

    @Override
    public void showAllSelect() {
        allSelect.setChecked(true);
    }

    @Override
    public void hideAllSelect() {
        allSelect.setChecked(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.deletes:
                mPresenter.deleteSentences(selectPresenter.getSelects());
                break;
            case R.id.fab_add_sentence:
                mPresenter.addSentence();
                break;
            case R.id.allSelect:
                selectPresenter.allSelectClick();
                break;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d(TAG, "onQueryTextSubmit = " + query);
        suggestions.saveRecentQuery(query, "asdf");
        if (mSearchView != null) {
            // 得到输入管理对象
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                // 这将让键盘在所有的情况下都被隐藏，但是一般我们在点击搜索按钮后，输入法都会乖乖的自动隐藏的。
                imm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0); // 输入法如果是显示状态，那么就隐藏输入法
            }
            mSearchView.clearFocus(); // 不获取焦点
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        Log.d(TAG,"onQueryTextChange"+newText);
        mPresenter.filterSentences(newText);
        return true;
    }

    private class SentencesAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        private List<Sentence> sentences;

        private SentencesSelectContract.Presenter selectPresenter;

        public List<Sentence> getSentences() {
            return sentences;
        }

        public void replace(List<Sentence> sentences){
            if(sentences != null){
                this.sentences.clear();
                this.sentences.addAll(sentences);
                notifyDataSetChanged();
                selectPresenter.dataSetChanged();//通知数据改变
            }
        }



        public SentencesAdapter(SentencesSelectContract.Presenter selectPresenter){
            sentences = new ArrayList<>();
            this.selectPresenter = selectPresenter;
        }

        @Override
        public int getCount() {
            return sentences.size();
        }

        @Override
        public Object getItem(int position) {
            return sentences.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(inflater == null){
                inflater = LayoutInflater.from(parent.getContext());
            }
            final ViewHolder viewHolder;
            if(convertView == null){
                convertView = inflater.inflate(R.layout.sentence_item,parent,false);
                viewHolder = new ViewHolder();
                viewHolder.select = (CheckBox) convertView.findViewById(R.id.select);
                viewHolder.content = (TextView) convertView.findViewById(R.id.content);
                viewHolder.translation = (TextView) convertView.findViewById(R.id.translation);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final Sentence sentence = sentences.get(position);
            viewHolder.content.setText(sentence.getContent());
            viewHolder.translation.setText(sentence.getTranslation());
            //是否显示复选框
            if(selectPresenter.isEdit()){
                if(selectPresenter.isSelect(sentence)){
                    viewHolder.select.setChecked(true);
                }else{
                    viewHolder.select.setChecked(false);
                }
                viewHolder.select.setVisibility(View.VISIBLE);
            }else{
                viewHolder.select.setVisibility(View.GONE);
            }
            viewHolder.select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectPresenter.onClick(sentence);
                }
            });
            return convertView;
        }

    }


    static class ViewHolder{
        CheckBox select;//选中
        TextView content;//内容
        TextView translation;//译文
    }
}
