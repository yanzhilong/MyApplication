package com.englishlearn.myapplication.wordgroup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.englishlearn.myapplication.MyApplication;
import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.data.WordGroup;
import com.englishlearn.myapplication.data.source.Repository;
import com.englishlearn.myapplication.sentence.ScrollChildSwipeRefreshLayout;
import com.englishlearn.myapplication.ui.LoadMoreListView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;


/**
 * Created by yanzl on 16-7-20.
 */
public class WordGroupsFragment extends Fragment {

    private static final String TAG = WordGroupsFragment.class.getSimpleName();

    private LoadMoreListView listview;
    private SwipeRefreshLayout srl;
    private MyAdapter myAdapter;

    private int page = 0;
    private final int PAGESIZE = 10;
    private boolean more = true;

    private List mList;
    private CompositeSubscription mSubscriptions;

    public static WordGroupsFragment newInstance() {
        return new WordGroupsFragment();
    }
    @Inject
    Repository repository;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.instance.getAppComponent().inject(this);
        mList = new ArrayList();
        if(mSubscriptions == null){
            mSubscriptions = new CompositeSubscription();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.wordgroups_frag, container, false);

        myAdapter = new MyAdapter();
        listview = (LoadMoreListView) root.findViewById(R.id.listview);
        srl = (SwipeRefreshLayout)root.findViewById(R.id.refresh_layout);
        listview.setAdapter(myAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.d(TAG,"onItemClick:" + position);
                WordGroup wordGroup = myAdapter.getList().get(position);
                Intent detail = new Intent(WordGroupsFragment.this.getContext(), WordsActivity.class);
                detail.putExtra(WordsActivity.OBJECT,wordGroup);
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
                (ScrollChildSwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
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

        //如果有设置菜单，需要加这个
        setHasOptionsMenu(true);

        return root;
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
        myAdapter.replace(mList);
        setLoadingIndicator(true);//显示刷新图标
        getNextPage();
    }

    //获取下一页
    public void getNextPage() {
        Subscription subscription = repository.getWordGroupsByOpenRx(page,PAGESIZE).subscribe(new Subscriber<List<WordGroup>>() {
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


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    private class MyAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        private List<WordGroup> wordGroups;

        public void replace(List<WordGroup> wordGroups){
            if(wordGroups != null){
                this.wordGroups.clear();
                this.wordGroups.addAll(wordGroups);
                notifyDataSetChanged();
            }
        }

        public List<WordGroup> getList() {
            return wordGroups;
        }

        public MyAdapter(){
            wordGroups = new ArrayList<>();
        }

        @Override
        public int getCount() {
            return wordGroups.size();
        }

        @Override
        public Object getItem(int position) {
            return wordGroups.get(position);
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
                convertView = inflater.inflate(R.layout.wordgroups_item,parent,false);
                viewHolder = new ViewHolder();
                viewHolder.name = (TextView) convertView.findViewById(R.id.name);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final WordGroup wordGroup = wordGroups.get(position);
            viewHolder.name.setText(wordGroup.getName());

            return convertView;
        }
    }

    static class ViewHolder{
        TextView name;//名称
    }
}
