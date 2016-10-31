package com.englishlearn.myapplication.tractategroup;

import android.os.Bundle;
import android.os.Handler;
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

import com.englishlearn.myapplication.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yanzl on 16-7-20.
 */
public class MyCreateTractateGroupFragment extends Fragment {

    public static final String OBJECT = "object";
    private static final String TAG = MyCreateTractateGroupFragment.class.getSimpleName();
    private final int PAGESIZE = 10;
    private Object object;
    private MyAdapter myAdapter;
    private int page = 0;
    private List<String> mList;

    private LinearLayoutManager mgrlistview;

    private SwipeRefreshLayout swipeRefreshLayout;//下拉刷新按钮

    public static MyCreateTractateGroupFragment newInstance() {
        return new MyCreateTractateGroupFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mList = new ArrayList();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.mycreatetractategroup_frag, container, false);

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

                String string = myAdapter.getStrings().get(position);
                Log.d(TAG, string.toString());

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
        getNextPage();
    }

    //获取下一页
    public void getNextPage() {

        List<String> strings = new ArrayList<>();
        for (int i = page * PAGESIZE; i < page * PAGESIZE + PAGESIZE; i++) {
            strings.add("test" + i);
        }

        Log.d(TAG, "onNext size:" + strings.size());

        if (strings == null || strings.size() == 0) {
            myAdapter.loadingGone();
            myAdapter.notifyDataSetChanged();
        } else {
            page++;//页数增加
            mList.addAll(strings);
            showList(mList);
        }
        loadingComplete();
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
        Log.d(TAG, "showList:" + list.toString());
        myAdapter.replaceData(list);
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
        private OnLoadMoreListener mOnLoadMoreListener;
        private List<String> strings;
        private OnItemClickListener onItemClickListener = null;

        public MyAdapter() {
            strings = new ArrayList<>();
        }

        //已经加载完成了
        public void loadingGone() {
            isGone = true;
        }

        //还有更多
        public void hasMore() {
            isGone = false;
        }

        public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
            this.mOnLoadMoreListener = mOnLoadMoreListener;
        }

        public List<String> getStrings() {
            return strings;
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        public void replaceData(List<String> strings) {
            if (strings != null) {
                this.strings.clear();
                this.strings.addAll(strings);
                notifyDataSetChanged();
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position != strings.size()) {
                Log.d(TAG, "wordgroupstop_item");
                return R.layout.mycreatetractategroup_frag_item;
            } else {
                if (isGone) {
                    Log.d(TAG, "load_done_layout");
                    return R.layout.mycreatetractategroup_frag_loaddone_item;
                }
                Log.d(TAG, "load_more_layout");
                return R.layout.mycreatetractategroup_frag_loadmore_item;
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
            switch (viewType) {
                case R.layout.mycreatetractategroup_frag_item:
                    return new ItemViewHolder(v);
                case R.layout.mycreatetractategroup_frag_loadmore_item:
                    return new LoadingMoreViewHolder(v);
                case R.layout.mycreatetractategroup_frag_loaddone_item:
                    return new LoadingGoneViewHolder(v);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Log.d(TAG, "onBindViewHolder" + position);
            if (holder instanceof ItemViewHolder) {
                ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                itemViewHolder.name.setText(strings.get(position));
            } else if (holder instanceof LoadingMoreViewHolder && mOnLoadMoreListener != null) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mOnLoadMoreListener.onLoadMore();
                    }
                }, 100);
            }
        }

        @Override
        public int getItemCount() {
            return strings.size() + 1;
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


