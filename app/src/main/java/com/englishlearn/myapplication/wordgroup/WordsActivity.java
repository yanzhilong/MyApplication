package com.englishlearn.myapplication.wordgroup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.englishlearn.myapplication.MyApplication;
import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.data.Word;
import com.englishlearn.myapplication.data.WordGroup;
import com.englishlearn.myapplication.data.source.Repository;
import com.englishlearn.myapplication.sentences.ScrollChildSwipeRefreshLayout;
import com.englishlearn.myapplication.ui.LoadMoreListView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class WordsActivity extends AppCompatActivity {

    public static final String OBJECT = "object";
    private String TAG = WordsActivity.class.getSimpleName();
    private WordGroup wordGroup;

    private LoadMoreListView listview;
    private SwipeRefreshLayout srl;
    private MyAdapter myAdapter;

    private int page = 0;
    private final int PAGESIZE = 10;
    private boolean more = true;

    private List mList;
    private CompositeSubscription mSubscriptions;

    @Inject
    Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words);

        MyApplication.instance.getAppComponent().inject(this);

        if(getIntent().hasExtra(OBJECT)){
            wordGroup = (WordGroup) getIntent().getSerializableExtra(OBJECT);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        //返回按钮
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        //标题
        ab.setTitle(R.string.title_phoneticsdetail);

        mList = new ArrayList();
        if(mSubscriptions == null){
            mSubscriptions = new CompositeSubscription();
        }

        myAdapter = new MyAdapter();
        listview = (LoadMoreListView) findViewById(R.id.listview);
        srl = (SwipeRefreshLayout)findViewById(R.id.refresh_layout);
        listview.setAdapter(myAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.d(TAG,"onItemClick:" + position);
                Word word = myAdapter.getList().get(position);
                Intent detail = new Intent(WordsActivity.this, WordsActivity.class);
                detail.putExtra(WordsActivity.OBJECT,word);
                startActivity(detail);
            }
        });

        listview.setOnLoadMoreLister(new LoadMoreListView.OnLoadMoreLister() {
            @Override
            public void loadingMore() {
                Log.d(TAG,"loadingMore current page:" + page);
                getNextPage();
            }
        });


        final ScrollChildSwipeRefreshLayout swipeRefreshLayout =
                (ScrollChildSwipeRefreshLayout)findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(this, R.color.colorPrimary),
                ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, R.color.colorPrimaryDark)
        );
        swipeRefreshLayout.setScrollUpChild(listview);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSubscriptions.clear();
                Log.d(TAG, "下拉刷新");
                refershList();
            }
        });

        refershList();//刷新列表

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume" );
    }

    @Override
    public void onPause() {
        super.onPause();
        setLoadingIndicator(false);
        mSubscriptions.clear();//取消订阅
    }

    //设置下拉图标
    public void setLoadingIndicator(final boolean active) {
        // Make sure setRefreshing() is called after the layout is done with everything else.
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });
    }

    //显示列表
    private void showList(List list){
        Log.d(TAG,"showList:" + list.toString());
        listview.setIsEnd(hasMore() ? false : true);
        myAdapter.replace(list);
        listview.loadingComplete();
    }

    //是否还有更多
    public boolean hasMore() {
        return more;
    }

    //加载完成
    public void loadingComplete(){
        Log.d(TAG,"loadingComplete");
        setLoadingIndicator(false);
    }

    //加载中
    public void loading(){
        Log.d(TAG,"loading");

    }

    /**
     * 加载失败
     */
    public void loadingFail(Throwable e){
        setLoadingIndicator(false);
        Log.d(TAG,"loadingFail:" + e.toString());
        e.printStackTrace();

    }

    /**
     * 刷新列表
     */
    public void refershList(){
        page = 0;
        mList.clear();
        setLoadingIndicator(true);//显示刷新图标
        getNextPage();
    }

    //获取下一页
    public void getNextPage() {
        Subscription subscription = repository.getWordsRxByWordGroupId(wordGroup.getObjectId(),page,PAGESIZE).subscribe(new Subscriber<List<Word>>() {
            @Override
            public void onCompleted() {
                loadingComplete();
            }

            @Override
            public void onError(Throwable e) {
                loadingFail(e);
            }

            @Override
            public void onNext(List list) {
                Log.d(TAG,"onNext size:" + list.size());
                if(list != null){
                    page++;//页数增加
                    mList.addAll(list);
                    more = list.size() == 0 ? false : true;
                }
                showList(mList);
            }
        });
        mSubscriptions.add(subscription);
    }


    private class MyAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        private List<Word> words;

        public void replace(List<Word> words){
            if(words != null){
                this.words.clear();
                this.words.addAll(words);
                notifyDataSetChanged();
            }
        }

        public List<Word> getList() {
            return words;
        }

        public MyAdapter(){
            words = new ArrayList<>();
        }

        @Override
        public int getCount() {
            return words.size();
        }

        @Override
        public Object getItem(int position) {
            return words.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(inflater == null){
                inflater = LayoutInflater.from(parent.getContext());
            }
            ViewHolder viewHolder;
            if(convertView == null){
                convertView = inflater.inflate(R.layout.words_item,parent,false);
                viewHolder = new ViewHolder();
                viewHolder.name = (TextView) convertView.findViewById(R.id.name);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final Word word = words.get(position);
            viewHolder.name.setText(word.getName());

            return convertView;
        }
    }

    static class ViewHolder{
        TextView name;//名称
    }
}
