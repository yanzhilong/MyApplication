package com.englishlearn.myapplication.tractategroup.tractatestop;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.englishlearn.myapplication.data.Tractate;
import com.englishlearn.myapplication.data.TractateType;
import com.englishlearn.myapplication.data.source.Repository;
import com.englishlearn.myapplication.tractategroup.tractate.TractateDetailActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;


/**
 * Created by yanzl on 16-7-20.
 */
public class TractateTopFragment extends Fragment {

    public static final String TRACTATETYPE = "TractateType";
    private static final String TAG = TractateTopFragment.class.getSimpleName();
    private final int PAGESIZE = 100;
    private TractateType tractateType;
    private MyAdapter myAdapter;
    private int page = 0;
    private List<Tractate> mList;

    private LinearLayoutManager mgrlistview;
    private CompositeSubscription mSubscriptions;
    private SwipeRefreshLayout swipeRefreshLayout;//下拉刷新按钮

    public static TractateTopFragment newInstance() {
        return new TractateTopFragment();
    }
    @Inject
    Repository repository;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyApplication.instance.getAppComponent().inject(this);
        if (getArguments() != null && getArguments().containsKey(TRACTATETYPE)) {
            tractateType = (TractateType) getArguments().getSerializable(TRACTATETYPE);
        }
        mList = new ArrayList();
        if (mSubscriptions == null) {
            mSubscriptions = new CompositeSubscription();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.tractatetop_frag, container, false);

        final RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recyclerview);
        //ListView效果的 LinearLayoutManager
        mgrlistview = new LinearLayoutManager(this.getContext());
        mgrlistview.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(mgrlistview);
        myAdapter = new MyAdapter();
        myAdapter.setOnItemClickListener(new RecyclerViewBaseAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {

                Tractate tractate = myAdapter.getTractates().get(position);
                Intent intent = new Intent(TractateTopFragment.this.getContext(),TractateDetailActivity.class);
                intent.putExtra(TractateDetailActivity.TRACTATE,tractate);
                startActivity(intent);
                Log.d(TAG, tractate.toString());

            }

        });

        myAdapter.setOnLoadMoreListener(new RecyclerViewBaseAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.d(TAG,"onLoadMore");
                getNextPageByTractateTypeId();
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

    /**
     * 刷新列表
     */
    public void refershList() {
        page = 0;
        mList.clear();
        myAdapter.hasMore();
        swipeRefreshLayout.setRefreshing(true);
        getNextPageByTractateTypeId();
    }

    //获取下一页
    public void getNextPageByTractateTypeId() {

        Log.d(TAG,"getNextPageByTractateTypeId");
        Subscription subscription = repository.getTractateRxByTractateTypeId(tractateType.getObjectId(),page,PAGESIZE).subscribe(new Subscriber<List<Tractate>>() {
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
    private void showList(List list) {
//        Log.d(TAG, "showList:" + list.toString());
        myAdapter.replaceData(list);
    }

    private class MyAdapter extends RecyclerViewBaseAdapter {

        private List<Tractate> tractates;

        public MyAdapter() {
            tractates = new ArrayList<>();
        }

        public List<Tractate> getTractates() {
            return tractates;
        }


        public void replaceData(List<Tractate> tractates) {
            if (tractates != null) {
                this.tractates.clear();
                this.tractates.addAll(tractates);
                notifyDataSetChanged();
            }
        }

        @Override
        public int getItemViewTypeBase(int position) {
            return R.layout.tractatetop_frag_item;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolderBase(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
            return new ItemViewHolder(v);
        }

        @Override
        public void onBindViewHolderBase(RecyclerView.ViewHolder holder, int position) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.name.setText(tractates.get(position).getTitle());
        }

        @Override
        public int getItemCountBase() {
            return tractates.size();
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


