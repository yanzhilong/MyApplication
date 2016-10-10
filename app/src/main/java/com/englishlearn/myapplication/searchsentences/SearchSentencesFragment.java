package com.englishlearn.myapplication.searchsentences;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.data.Sentence;
import com.englishlearn.myapplication.sentencedetail.SentenceDetailActivity;
import com.englishlearn.myapplication.sentence.ScrollChildSwipeRefreshLayout;
import com.englishlearn.myapplication.ui.LoadMoreListView;
import com.englishlearn.myapplication.util.SearchUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanzl on 16-7-20.
 */
public class SearchSentencesFragment extends Fragment implements SearchSentencesContract.View, View.OnClickListener, TextView.OnEditorActionListener, TextWatcher {

    private static final String TAG = SearchSentencesFragment.class.getSimpleName();
    private SentencesAdapter sentencesAdapter;
    private SearchSentencesContract.Presenter mPresenter;
    private LoadMoreListView sentences_listview;
    private SwipeRefreshLayout srl;
    private EditText searchEditText;
    private String mSerachWord = "";

    public static SearchSentencesFragment newInstance() {
        return new SearchSentencesFragment();
    }

    @Override
    public void setPresenter(SearchSentencesContract.Presenter presenter) {
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
        View root = inflater.inflate(R.layout.searchsentences_frag, container, false);

        sentencesAdapter = new SentencesAdapter();
        sentences_listview = (LoadMoreListView) root.findViewById(R.id.sentences_listview);
        sentences_listview.setAdapter(sentencesAdapter);

        srl = (SwipeRefreshLayout)root.findViewById(R.id.refresh_layout);

        Toolbar toolbar =  (Toolbar) getActivity().findViewById(R.id.toolbar);
        ImageButton search = (ImageButton) toolbar.findViewById(R.id.search);
        searchEditText = (EditText) toolbar.findViewById(R.id.search_edit);
        searchEditText.setOnEditorActionListener(this);
        searchEditText.addTextChangedListener(this);
        searchEditText.requestFocus();
        search.setOnClickListener(this);

        sentences_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position >= sentencesAdapter.getSentences().size()){
                    //底部加戴Item
                    return;
                }else{
                    Sentence sentence = sentencesAdapter.getSentences().get(position);
                    Intent detail = new Intent(SearchSentencesFragment.this.getContext(), SentenceDetailActivity.class);
                    detail.putExtra(SentenceDetailActivity.ID,sentence.getObjectId());
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
                Log.d(TAG, "下拉刷新");
                mPresenter.refreshSentences();
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
        sentences_listview.loadingComplete();
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
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search:
                submit(true);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            submit(true);
        }
        return false;
    }

    private void submit(boolean complete){

        if(complete){
            // 得到输入管理对象
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                // 这将让键盘在所有的情况下都被隐藏，但是一般我们在点击搜索按钮后，输入法都会乖乖的自动隐藏的。
                imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0); // 输入法如果是显示状态，那么就隐藏输入法
            }
            searchEditText.clearFocus();
        }
        mPresenter.unsubscribe();//取消请求

        mSerachWord = searchEditText.getText().toString();
        Log.d(TAG,"submit:" + mSerachWord);

        mPresenter.getSentences(mSerachWord);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        submit(false);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private class SentencesAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        private List<Sentence> sentences;

        public List<Sentence> getSentences() {
            return sentences;
        }

        public void replace(List<Sentence> sentences){
            if(sentences != null){
                this.sentences.clear();
                this.sentences.addAll(sentences);
                notifyDataSetChanged();
            }
        }

        public SentencesAdapter(){
            sentences = new ArrayList<>();
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
                convertView = inflater.inflate(R.layout.searchsentence_item,parent,false);
                viewHolder = new ViewHolder();
                viewHolder.content = (TextView) convertView.findViewById(R.id.content);
                viewHolder.translation = (TextView) convertView.findViewById(R.id.translation);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final Sentence sentence = sentences.get(position);

            if(mSerachWord != null && !mSerachWord.equals("")){

                String result;
                if(SearchUtil.getInstance().isContainsChinese(mSerachWord)){
                    result = sentence.getTranslation();
                }else{
                    result = sentence.getContent();
                }
                Spannable spannable = SearchUtil.getInstance().getSpannable(mSerachWord,result,inflater.getContext());
                viewHolder.content.setText(spannable);
            }else{
                viewHolder.content.setText(sentence.getContent());
            }

            return convertView;
        }

    }

    static class ViewHolder{
        TextView content;//内容
        TextView translation;//译文
    }
}
