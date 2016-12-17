package com.englishlearn.myapplication.tractategroup;

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
import com.englishlearn.myapplication.core.NewIntentInterface;
import com.englishlearn.myapplication.data.TractateType;
import com.englishlearn.myapplication.data.source.Repository;
import com.englishlearn.myapplication.tractategroup.tractatestop.TractatesTopActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * 文章类型列表
 */
public class TractateTypesFragment extends Fragment implements NewIntentInterface {

    public static final String OBJECT = "object";
    private static final String TAG = TractateTypesFragment.class.getSimpleName();
    private Object object;
    private MyAdapter myAdapter;
    private List<TractateType> mList;
    private CompositeSubscription mSubscriptions;

    private LinearLayoutManager mgrlistview;

    private SwipeRefreshLayout swipeRefreshLayout;//下拉刷新按钮

    @Inject
    Repository repository;

    public static TractateTypesFragment newInstance() {
        return new TractateTypesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyApplication.instance.getAppComponent().inject(this);

        if (getArguments() != null && getArguments().containsKey(OBJECT)) {
            object = getArguments().getSerializable(OBJECT);
        }
        mList = new ArrayList();

        if (mSubscriptions == null) {
            mSubscriptions = new CompositeSubscription();
        }

    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.tractatetypes_frag, container, false);

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

                /*TractateType tractateType = myAdapter.getTractateTypes().get(position);
                Intent intent = new Intent(TractateTypesFragment.this.getContext(), Tractates1Activity.class);
                intent.putExtra(Tractates1Activity.TRACTATETYPE,tractateType);
                startActivity(intent);
                Log.d(TAG, tractateType.toString());*/

                TractateType tractateType = myAdapter.getTractateTypes().get(position);
                Intent intent = new Intent(TractateTypesFragment.this.getContext(), TractatesTopActivity.class);
                intent.putExtra(TractatesTopActivity.TRACTATETYPE,tractateType);
                startActivity(intent);
                Log.d(TAG, tractateType.toString());

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
                getList();
            }
        });

        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
        getList();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    void getList() {


        swipeRefreshLayout.setRefreshing(true);

        Subscription subscription = repository.getTractateTypesRx().subscribe(new Subscriber<List<TractateType>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                loadingFail(e);
            }

            @Override
            public void onNext(List list) {
                if(list != null && list.size() > 0){
                    Log.d(TAG,"onNext size:" + list.size());
                    mList.clear();
                    mList.addAll(list);
                    showList(mList);
                }
            }
        });
        mSubscriptions.add(subscription);

        showList(mList);
    }

    /**
     * 加载失败
     */
    public void loadingFail(Throwable e){
        Log.d(TAG,"loadingFail:" + e.toString());
        e.printStackTrace();

    }

    void showList(List<TractateType> list) {

        swipeRefreshLayout.setRefreshing(false);
        myAdapter.replaceData(list);
    }

    @Override
    public void onNewIntent(Intent intent) {
        getList();
    }


    //接口
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private List<TractateType> tractateTypes;

        private OnItemClickListener onItemClickListener = null;

        public MyAdapter() {
            tractateTypes = new ArrayList<>();
        }

        public List<TractateType> getTractateTypes() {
            return tractateTypes;
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        public void replaceData(List<TractateType> tractateTypes) {
            if (tractateTypes != null) {
                this.tractateTypes.clear();
                this.tractateTypes.addAll(tractateTypes);
                notifyDataSetChanged();
            }
        }

        //该方法返回是ViewHolder，当有可复用View时，就不再调用
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tractatetypes_frag_item, parent, false);
            return new ViewHolder(v);
        }

        //将数据绑定到子View，会自动复用View
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            Log.d(TAG, "onBindViewHolder" + position);
            holder.textView.setText(tractateTypes.get(position).getName());
        }

        @Override
        public int getItemCount() {
            return tractateTypes.size();
        }


        //自定义的ViewHolder,减少findViewById调用次数
        class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            public ViewHolder(final View itemView) {
                super(itemView);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(itemView, getAdapterPosition());
                        }
                    }
                });

                textView = (TextView) itemView.findViewById(R.id.name);
            }
        }
    }


}
