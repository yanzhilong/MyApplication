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
import com.englishlearn.myapplication.adapter.RecyclerViewBaseAdapter;
import com.englishlearn.myapplication.core.NewIntentInterface;
import com.englishlearn.myapplication.data.User;
import com.englishlearn.myapplication.data.WordGroup;
import com.englishlearn.myapplication.data.source.Repository;
import com.englishlearn.myapplication.wordgroups.words.WordGroupType;
import com.englishlearn.myapplication.wordgroups.words.WordsActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;


/**
 * Created by yanzl on 16-7-20.
 */
public class WordGroupsTopFragment extends Fragment implements NewIntentInterface{

    private static final String TAG = WordGroupsTopFragment.class.getSimpleName();

    private MyAdapter myAdapter;

    private int page = 0;
    private final int PAGESIZE = 20;

    private List<WordGroup> mList;
    private User user;
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

        user = repository.getUserInfo();

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
        myAdapter.setOnItemClickListener(new RecyclerViewBaseAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {

                WordGroup wordGroup = myAdapter.getWordGroups().get(position);
                Log.d(TAG, wordGroup.toString());
                Intent intent = new Intent(WordGroupsTopFragment.this.getContext(), WordsActivity.class);
                intent.putExtra(WordsActivity.WORDGROUP, wordGroup);
                intent.putExtra(WordsActivity.TYPE, WordGroupType.OTHERWGROUP);
                startActivity(intent);
            }

        });

        myAdapter.setOnLoadMoreListener(new RecyclerViewBaseAdapter.OnLoadMoreListener() {
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

        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
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
    public void refershList() {
        page = 0;
        mList.clear();
        myAdapter.replaceData(mList);
        myAdapter.hasMore();
        swipeRefreshLayout.setRefreshing(true);
        getNextPage();
    }

    //获取下一页
    public void getNextPage() {
        Subscription subscription = repository.getWordGroupsByOpenRx(page, PAGESIZE).subscribe(new Subscriber<List<WordGroup>>() {
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
                Log.d(TAG, "onNext size:" + list.size());

                if (list == null || list.size() == 0) {
                    myAdapter.loadingGone();
                    myAdapter.notifyDataSetChanged();
                } else {
                    page++;//页数增加
                    mList.addAll(list);
                    showList(mList);
                }
            }
        });
        mSubscriptions.add(subscription);
    }

    //加载完成
    public void loadingComplete() {
        Log.d(TAG, "loadingComplete");
        swipeRefreshLayout.setRefreshing(false);
    }

    /**
     * 加载失败
     */
    public void loadingFail(Throwable e) {
        Log.d(TAG, "loadingFail:" + e.toString());
        e.printStackTrace();
        swipeRefreshLayout.setRefreshing(false);
    }

    //显示列表
    private void showList(List list) {
        Log.d(TAG, "showList:" + list.toString());
        myAdapter.replaceData(list);
    }

    @Override
    public void onNewIntent(Intent intent) {
        refershList();
    }


    //接口
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    //加载更多接口
    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    private class MyAdapter extends RecyclerViewBaseAdapter {


        private List<WordGroup> wordGroups;

        public MyAdapter() {
            wordGroups = new ArrayList<>();
        }

        public List<WordGroup> getWordGroups() {
            return wordGroups;
        }

        public void replaceData(List<WordGroup> wordGroups) {
            if (wordGroups != null) {
                this.wordGroups.clear();
                this.wordGroups.addAll(wordGroups);
                notifyDataSetChanged();
            }
        }

        @Override
        public int getItemViewTypeBase(int position) {
            return R.layout.wordgroupstop_item;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolderBase(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
            return new ItemViewHolder(v);
        }

        @Override
        public void onBindViewHolderBase(RecyclerView.ViewHolder holder, int position) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.name.setText(wordGroups.get(position).getName());
        }

        @Override
        public int getItemCountBase() {
            return wordGroups.size();
        }


        //自定义的ViewHolder,减少findViewById调用次数
        class ItemViewHolder extends RecyclerViewBaseAdapter.ViewHolder {
            TextView name;

            public ItemViewHolder(final View itemView) {
                super(itemView);

                name = (TextView) itemView.findViewById(R.id.name);
            }
        }
    }

}
