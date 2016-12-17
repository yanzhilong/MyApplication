package com.englishlearn.myapplication.sentencegroups;

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
import com.englishlearn.myapplication.data.SentenceGroupCollect;
import com.englishlearn.myapplication.data.User;
import com.englishlearn.myapplication.data.source.Repository;
import com.englishlearn.myapplication.sentencegroups.sentences.SentenceGroupType;
import com.englishlearn.myapplication.sentencegroups.sentences.SentencesActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;


/**
 * Created by yanzl on 16-7-20.
 */
public class MyCollectSentenceGroupsFragment extends Fragment implements NewIntentInterface {

    public static final String OBJECT = "object";
    private static final String TAG = MyCollectSentenceGroupsFragment.class.getSimpleName();
    private final int PAGESIZE = 10;
    private Object object;
    private MyAdapter myAdapter;
    private int page = 0;
    private List<SentenceGroupCollect> sentenceGroupCollects;

    private User user;
    private CompositeSubscription mSubscriptions;
    @Inject
    Repository repository;
    private LinearLayoutManager mgrlistview;

    private SwipeRefreshLayout swipeRefreshLayout;//下拉刷新按钮

    public static MyCollectSentenceGroupsFragment newInstance() {
        return new MyCollectSentenceGroupsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyApplication.instance.getAppComponent().inject(this);

        user = repository.getUserInfo();
        sentenceGroupCollects = new ArrayList<>();

        if (mSubscriptions == null) {
            mSubscriptions = new CompositeSubscription();
        }

    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.mycollectsentencegroups_frag, container, false);

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

                SentenceGroupCollect sentenceGroupCollect = myAdapter.getSentenceGroupCollects().get(position);
                Log.d(TAG, sentenceGroupCollect.toString());
                Intent intent = new Intent(MyCollectSentenceGroupsFragment.this.getContext(), SentencesActivity.class);


                Bundle bundle = new Bundle();

                bundle.putSerializable(SentencesActivity.SENTENCEGROUPCOLLECT, sentenceGroupCollect);
                bundle.putSerializable(SentencesActivity.TYPE, SentenceGroupType.FAVORITESGROUP);
                intent.putExtras(bundle);
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

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
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
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onNewIntent(Intent intent) {
        Log.d(TAG,"onNewIntent");
        refershList();
    }

    /**
     * 刷新列表
     */
    public void refershList() {
        page = 0;
        sentenceGroupCollects.clear();
        myAdapter.replaceData(sentenceGroupCollects);
        myAdapter.hasMore();
        swipeRefreshLayout.setRefreshing(true);
        getNextPage();
    }


    //获取下一页
    public void getNextPage() {

        Subscription subscription = repository.getSentenceGroupCollectRxByUserId(user.getObjectId(), page, PAGESIZE).subscribe(new Subscriber<List<SentenceGroupCollect>>() {
            @Override
            public void onCompleted() {
                loadingComplete();
            }

            @Override
            public void onError(Throwable e) {
                loadingFail(e);
            }

            @Override
            public void onNext(List<SentenceGroupCollect> list) {
                Log.d(TAG, "onNext size:" + list.size());

                if (list == null || list.size() == 0) {
                    myAdapter.loadingGone();
                    myAdapter.notifyDataSetChanged();
                } else {
                    sentenceGroupCollects.addAll(list);
                    page++;//页数增加
                    //mCollectSentenceGroupList.addAll(list);
                    showList(sentenceGroupCollects);
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

    }

    //显示列表
    private void showList(List<SentenceGroupCollect> list) {
        Log.d(TAG, "showList:" + list.toString());
        myAdapter.replaceData(list);
    }

    private class MyAdapter extends RecyclerViewBaseAdapter {

        private List<SentenceGroupCollect> sentenceGroupCollects;

        public MyAdapter() {
            sentenceGroupCollects = new ArrayList<>();
        }


        public List<SentenceGroupCollect> getSentenceGroupCollects() {
            return sentenceGroupCollects;
        }

        public void replaceData(List<SentenceGroupCollect> sentenceGroupCollects) {
            if (sentenceGroupCollects != null) {
                this.sentenceGroupCollects.clear();
                this.sentenceGroupCollects.addAll(sentenceGroupCollects);
                notifyDataSetChanged();
            }
        }

        @Override
        public int getItemViewTypeBase(int position) {
            return R.layout.sentencegroupstopfragment_frag_item;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolderBase(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
            return new ItemViewHolder(v);
        }

        @Override
        public void onBindViewHolderBase(RecyclerView.ViewHolder holder, int position) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.name.setText(sentenceGroupCollects.get(position).getSentenceGroup().getName());
        }

        @Override
        public int getItemCountBase() {
            return sentenceGroupCollects.size();
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


