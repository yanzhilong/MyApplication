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
import com.englishlearn.myapplication.core.NewIntentInterface;
import com.englishlearn.myapplication.data.SentenceCollectGroup;
import com.englishlearn.myapplication.data.source.Repository;
import com.englishlearn.myapplication.sentencegroups.sentences.SentenceGroupType;
import com.englishlearn.myapplication.sentencegroups.sentences.SentencesActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;


public class MyCreateSentenceCollectGroupsFragment extends Fragment implements NewIntentInterface {

    public static final String OBJECT = "object";
    private static final String TAG = MyCreateSentenceCollectGroupsFragment.class.getSimpleName();
    private Object object;
    private MyAdapter myAdapter;
    private List<SentenceCollectGroup> sentenceCollectGroup;
    private LinearLayoutManager mgrlistview;

    private SwipeRefreshLayout swipeRefreshLayout;//下拉刷新按钮
    private CompositeSubscription mSubscriptions;
    @Inject
    Repository repository;

    public static MyCreateSentenceCollectGroupsFragment newInstance() {
        return new MyCreateSentenceCollectGroupsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(OBJECT)) {
            object = getArguments().getSerializable(OBJECT);
        }

        MyApplication.instance.getAppComponent().inject(this);
        sentenceCollectGroup = new ArrayList<>();

        if (mSubscriptions == null) {
            mSubscriptions = new CompositeSubscription();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.mycreatesentencecollectgroups_frag, container, false);

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

                SentenceCollectGroup sentenceCollectGroup = myAdapter.getStrings().get(position);
                Log.d(TAG, sentenceCollectGroup.toString());
                Intent intent = new Intent(MyCreateSentenceCollectGroupsFragment.this.getContext(),SentencesActivity.class);

                Bundle bundle = new Bundle();

                bundle.putSerializable(SentencesActivity.SENTENCECOLLECTGROUP,sentenceCollectGroup);
                bundle.putSerializable(SentencesActivity.TYPE, SentenceGroupType.CREATEFSGROUP);
                intent.putExtras(bundle);
                startActivity(intent);

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
                getSentenceCollectGroups();
            }
        });

        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
        getSentenceCollectGroups();
    }

    @Override
    public void onPause() {
        super.onPause();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onNewIntent(Intent intent) {
        Log.d(TAG,"onNewIntent");
        getSentenceCollectGroups();
    }


    //获取我创建的收藏分组
    private void getSentenceCollectGroups(){

        sentenceCollectGroup.clear();
        swipeRefreshLayout.setRefreshing(true);
        Subscription subscription = repository.getSentenceCollectGroupRxByUserId(repository.getUserInfo().getObjectId()).subscribe(new Subscriber<List<SentenceCollectGroup>>() {
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
                    sentenceCollectGroup.addAll(list);
                    showList(sentenceCollectGroup);
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

    void showList(List<SentenceCollectGroup> list) {

        swipeRefreshLayout.setRefreshing(false);
        myAdapter.replaceData(list);
    }


    //接口
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private List<SentenceCollectGroup> strings;

        private OnItemClickListener onItemClickListener = null;

        public MyAdapter() {
            strings = new ArrayList<>();
        }

        public List<SentenceCollectGroup> getStrings() {
            return strings;
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        public void replaceData(List<SentenceCollectGroup> strings) {
            if (strings != null) {
                this.strings.clear();
                this.strings.addAll(strings);
                notifyDataSetChanged();
            }
        }

        //该方法返回是ViewHolder，当有可复用View时，就不再调用
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mycreatesentencecollectgroups_frag_item, parent, false);
            return new ViewHolder(v);
        }

        //将数据绑定到子View，会自动复用View
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            Log.d(TAG, "onBindViewHolder" + position);
            holder.textView.setText(strings.get(position).getName());
        }

        @Override
        public int getItemCount() {
            return strings.size();
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
