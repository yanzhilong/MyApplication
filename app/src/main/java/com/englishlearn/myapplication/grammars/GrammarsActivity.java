package com.englishlearn.myapplication.grammars;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.englishlearn.myapplication.MyApplication;
import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.data.Grammar;
import com.englishlearn.myapplication.data.source.Repository;
import com.englishlearn.myapplication.grammardetail.GrammarDetailActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class GrammarsActivity extends AppCompatActivity {

    private static final String TAG = GrammarsActivity.class.getSimpleName();

    private MyAdapter myAdapter;

    private CompositeSubscription mSubscriptions;
    @Inject
    Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grammars_act);

        MyApplication.instance.getAppComponent().inject(this);

        if (mSubscriptions == null) {
            mSubscriptions = new CompositeSubscription();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        //返回按钮
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        //标题
        ab.setTitle(R.string.title_语法列表);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        //ListView效果的 LinearLayoutManager
        final LinearLayoutManager mgrlistview = new LinearLayoutManager(this);
        mgrlistview.setOrientation(LinearLayoutManager.VERTICAL);

        //GridLayout 3列
        GridLayoutManager mgrgridview = new GridLayoutManager(this, 3);

        recyclerView.setLayoutManager(mgrlistview);

        //recyclerView.setLayoutManager(mgrgridview);
        myAdapter = new MyAdapter();
        myAdapter.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {

                Grammar grammar = myAdapter.getGrammars().get(position);
                Log.d(TAG, myAdapter.getGrammars().get(position).toString());
                Intent intent = new Intent(GrammarsActivity.this,GrammarDetailActivity.class);
                intent.putExtra(GrammarDetailActivity.OBJECT,grammar);
                startActivity(intent);
            }

        });
        //设置适配器
        recyclerView.setAdapter(myAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getList();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    void getList() {
        Subscription subscription = repository.getGrammars()
                .subscribe(new Subscriber<List<Grammar>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        showPhoneticsSymbolsFail();
                    }

                    @Override
                    public void onNext(List<Grammar> grammars) {
                        if(grammars != null){
                            showList(grammars);
                        }
                    }
                });
        mSubscriptions.add(subscription);
    }

    void showPhoneticsSymbolsFail() {
        Toast.makeText(this,R.string.networkerror,Toast.LENGTH_SHORT).show();

    }

    void showList(List<Grammar> list) {
        myAdapter.replaceData(list);
    }

    //接口
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private List<Grammar> grammars;

        private OnItemClickListener onItemClickListener = null;

        public MyAdapter() {
            grammars = new ArrayList<>();
        }

        public List<Grammar> getGrammars() {
            return grammars;
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        public void replaceData(List<Grammar> grammars) {
            if (grammars != null) {
                this.grammars.clear();
                this.grammars.addAll(grammars);
                notifyDataSetChanged();
            }
        }

        //该方法返回是ViewHolder，当有可复用View时，就不再调用
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grammars_act_item, parent, false);
            return new ViewHolder(v);
        }

        //将数据绑定到子View，会自动复用View
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            Log.d(TAG, "onBindViewHolder" + position);
            holder.textView.setText(grammars.get(position).getTitle());
        }

        @Override
        public int getItemCount() {
            return grammars.size();
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


