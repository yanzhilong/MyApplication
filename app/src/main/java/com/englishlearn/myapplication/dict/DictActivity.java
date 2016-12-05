package com.englishlearn.myapplication.dict;

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
import com.englishlearn.myapplication.core.DownloadManager;
import com.englishlearn.myapplication.core.MdictManager;
import com.englishlearn.myapplication.data.Dict;
import com.englishlearn.myapplication.data.DownloadStatus;
import com.englishlearn.myapplication.data.source.Repository;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobRequestException;
import com.englishlearn.myapplication.observer.DownloadObserver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.inject.Inject;

import cn.mdict.mdx.MdxEngine;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class DictActivity extends AppCompatActivity {

    private static final String TAG = DictActivity.class.getSimpleName();

    private MyAdapter myAdapter;
    private List<Dict> mList;
    private CompositeSubscription mSubscriptions;
    @Inject
    Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dict_act);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        //返回按钮
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        //标题
        ab.setTitle(R.string.title_dict);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        //ListView效果的 LinearLayoutManager
        final LinearLayoutManager mgrlistview = new LinearLayoutManager(this);
        mgrlistview.setOrientation(LinearLayoutManager.VERTICAL);

        //GridLayout 3列
        GridLayoutManager mgrgridview = new GridLayoutManager(this, 3);

        recyclerView.setLayoutManager(mgrlistview);

        MyApplication.instance.getAppComponent().inject(this);

        mList = new ArrayList();
        if (mSubscriptions == null) {
            mSubscriptions = new CompositeSubscription();
        }

        //recyclerView.setLayoutManager(mgrgridview);
        myAdapter = new MyAdapter();
        myAdapter.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                String mdictHome = DictActivity.this.getFilesDir().getAbsolutePath() + "/mdict/doc";
                Log.d(TAG, myAdapter.getStrings().get(position).toString());
                Dict dict = myAdapter.getStrings().get(position);

                DownloadObserver.newInstance().addObserver(new Observer() {
                    @Override
                    public void update(Observable observable, Object data) {
                        DownloadStatus downloadStatus = (DownloadStatus) data;
                        if(downloadStatus.isException()){
                            Toast.makeText(DictActivity.this,"下载失败了",Toast.LENGTH_SHORT).show();

                        }else{
                            Log.d(TAG,"downFIle Currentsize:" + downloadStatus.getCurrentsizestr() + " Size:" + downloadStatus.getSizeStr() + "Percent:" + downloadStatus.getPercent());
                        }
                        if(downloadStatus.isSuccess()){

                            MdxEngine.refresh();
                            MdictManager.newInstance(DictActivity.this).initMdict();
                            Toast.makeText(DictActivity.this,"下载成功",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                DownloadManager.downLoadFile(mdictHome + File.separator + "taoge.mdx",dict.getFile().getUrl());
                Toast.makeText(DictActivity.this,"正在下载",Toast.LENGTH_SHORT).show();

            }

        });
        //设置适配器
        recyclerView.setAdapter(myAdapter);
        getList();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    void getList() {

        Subscription subscription = repository.getDicts().subscribe(new Subscriber<List<Dict>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(e instanceof BmobRequestException){
                    BmobRequestException bmobRequestException = (BmobRequestException) e;
                    Toast.makeText(DictActivity.this,bmobRequestException.getMessage(),Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(DictActivity.this,DictActivity.this.getString(R.string.networkerror),Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onNext(List<Dict> dicts) {
                if(dicts != null){
                    showList(dicts);
                }
            }
        });
        mSubscriptions.add(subscription);
    }

    void showList(List<Dict> list) {
        myAdapter.replaceData(list);
    }

    //接口
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private List<Dict> strings;

        private OnItemClickListener onItemClickListener = null;

        public MyAdapter() {
            strings = new ArrayList<>();
        }

        public List<Dict> getStrings() {
            return strings;
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        public void replaceData(List<Dict> strings) {
            if (strings != null) {
                this.strings.clear();
                this.strings.addAll(strings);
                notifyDataSetChanged();
            }
        }

        //该方法返回是ViewHolder，当有可复用View时，就不再调用
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dict_act_item, parent, false);
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


