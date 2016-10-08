package com.englishlearn.myapplication.wordgroups;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.englishlearn.myapplication.MyApplication;
import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.data.WordGroup;
import com.englishlearn.myapplication.data.source.Repository;
import com.englishlearn.myapplication.grammardetail.GrammarDetailActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;


/**
 * Created by yanzl on 16-7-20.
 */
public class WordGroupsTopFragment extends Fragment {

    private static final String TAG = WordGroupsTopFragment.class.getSimpleName();

    private MyAdapter myAdapter;

    private int page = 0;
    private final int PAGESIZE = 10;

    private List<WordGroup> mList;

    private CompositeSubscription mSubscriptions;
    @Inject
    Repository repository;
    private LinearLayoutManager mgrlistview;

    private SwipeRefreshLayout swipeRefreshLayout;//下拉刷新按钮

    public static WordGroupsTopFragment newInstance() {
        return new WordGroupsTopFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyApplication.instance.getAppComponent().inject(this);

        mList = new ArrayList();
        if (mSubscriptions == null) {
            mSubscriptions = new CompositeSubscription();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.wordgroupstop_frag, container, false);

        final RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recyclerview);
        //ListView效果的 LinearLayoutManager
        mgrlistview = new LinearLayoutManager(this.getContext());
        mgrlistview.setOrientation(LinearLayoutManager.VERTICAL);

        //GridLayout 3列
        GridLayoutManager mgrgridview = new GridLayoutManager(this.getContext(), 3);

        recyclerView.setLayoutManager(mgrlistview);

        //recyclerView.setLayoutManager(mgrgridview);
        myAdapter = new MyAdapter();
        myAdapter.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {

                WordGroup wordGroup = myAdapter.getWordGroups().get(position);
                Log.d(TAG, wordGroup.toString());
                Intent intent = new Intent(WordGroupsTopFragment.this.getContext(),GrammarDetailActivity.class);
                intent.putExtra(GrammarDetailActivity.OBJECT,wordGroup);
                startActivity(intent);
            }

        });

        myAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getNextPage();
            }
        });
        //设置适配器
        recyclerView.setAdapter(myAdapter);


        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSubscriptions.clear();
                Log.d(TAG, "下拉刷新");
                refershList();
            }
        });

        refershList();//刷新列表

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mSubscriptions.clear();
        swipeRefreshLayout.setRefreshing(false);
    }

    /**
     * 刷新列表
     */
    public void refershList(){
        page = 0;
        mList.clear();
        myAdapter.hasMore();
        swipeRefreshLayout.setRefreshing(true);
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

                if(list == null || list.size() == 0){
                    myAdapter.loadingGone();
                    myAdapter.notifyDataSetChanged();
                }else{
                    page++;//页数增加
                    mList.addAll(list);
                    showList(mList);
                }
            }
        });
        mSubscriptions.add(subscription);
    }

    //加载完成
    public void loadingComplete(){
        Log.d(TAG,"loadingComplete");
        swipeRefreshLayout.setRefreshing(false);
    }

    /**
     * 加载失败
     */
    public void loadingFail(Throwable e){
        Log.d(TAG,"loadingFail:" + e.toString());
        e.printStackTrace();

    }

    //显示列表
    private void showList(List list){
        Log.d(TAG,"showList:" + list.toString());

        if(list == null || list.size() == 0){
            myAdapter.loadingGone();
            myAdapter.notifyDataSetChanged();
        }else{
            myAdapter.replaceData(list);
        }
    }


    //接口
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    //加载更多接口
    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    private class MyAdapter extends RecyclerView.Adapter {

        private boolean isGone = false;//是否加载完成

        //已经加载完成了
        public void loadingGone(){
            isGone = true;
        }

        //还有更多
        public void hasMore(){
            isGone = false;
        }

        private OnLoadMoreListener mOnLoadMoreListener;

        public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
            this.mOnLoadMoreListener = mOnLoadMoreListener;
        }

        private List<WordGroup> wordGroups;

        private OnItemClickListener onItemClickListener = null;

        public MyAdapter() {
            wordGroups = new ArrayList<>();
        }

        public List<WordGroup> getWordGroups() {
            return wordGroups;
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        public void replaceData(List<WordGroup> wordGroups) {
            if (wordGroups != null) {
                this.wordGroups.clear();
                this.wordGroups.addAll(wordGroups);
                notifyDataSetChanged();
            }
        }

        @Override
        public int getItemViewType(int position) {
            if(position != wordGroups.size() ){
                Log.d(TAG,"wordgroupstop_item");
                return R.layout.wordgroupstop_item;
            }else{
                if(isGone){
                    Log.d(TAG,"load_done_layout");
                    return R.layout.load_done_layout;
                }
                Log.d(TAG,"load_more_layout");
                return R.layout.load_more_layout;
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
            switch (viewType){
                case R.layout.wordgroupstop_item:
                    return new ItemViewHolder(v);
                case R.layout.load_more_layout:
                    return new LoadingMoreViewHolder(v);
                case R.layout.load_done_layout:
                    return new LoadingGoneViewHolder(v);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Log.d(TAG, "onBindViewHolder" + position);
            if(holder instanceof ItemViewHolder){
                ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                itemViewHolder.name.setText(wordGroups.get(position).getName());
            }else if(holder instanceof LoadingMoreViewHolder && mOnLoadMoreListener != null){
                mOnLoadMoreListener.onLoadMore();
            }
        }

        @Override
        public int getItemCount() {
            return wordGroups.size() + 1;
        }


        //自定义的ViewHolder,减少findViewById调用次数
        class ItemViewHolder extends RecyclerView.ViewHolder {
            TextView name;

            public ItemViewHolder(final View itemView) {
                super(itemView);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(itemView, getAdapterPosition());
                        }
                    }
                });

                name = (TextView) itemView.findViewById(R.id.name);
            }
        }
        //加载更多
        class LoadingMoreViewHolder extends RecyclerView.ViewHolder {

            public LoadingMoreViewHolder(View itemView) {
                super(itemView);
            }
        }
        //加载完成
        class LoadingGoneViewHolder extends RecyclerView.ViewHolder {

            public LoadingGoneViewHolder(View itemView) {
                super(itemView);
            }
        }
    }

}
