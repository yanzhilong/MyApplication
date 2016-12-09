package com.englishlearn.myapplication.dict;

import android.app.DownloadManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.englishlearn.myapplication.MyApplication;
import com.englishlearn.myapplication.R;
import com.englishlearn.myapplication.core.DownloadManagerStatus;
import com.englishlearn.myapplication.core.DownloadUtil;
import com.englishlearn.myapplication.core.MdictManager;
import com.englishlearn.myapplication.data.Dict;
import com.englishlearn.myapplication.data.source.Repository;
import com.englishlearn.myapplication.data.source.preferences.SharedPreferencesDataSource;
import com.englishlearn.myapplication.data.source.remote.bmob.BmobRequestException;
import com.englishlearn.myapplication.observer.DownloadUtilObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;


import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class DictActivity extends AppCompatActivity {

    private static final String TAG = DictActivity.class.getSimpleName();

    private MyAdapter myAdapter;
    private HashMap<String, Long> downloadIdMap;
    private HashMap<String, DownloadManagerStatus> downloadManagerStatusHashMap;
    private CompositeSubscription mSubscriptions;
    private Dict mLocaldict;

    @Inject
    Repository repository;

    @Inject
    SharedPreferencesDataSource sharedPreferencesDataSource;
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
        downloadIdMap = new HashMap<>();
        downloadManagerStatusHashMap = new HashMap<>();
        if (mSubscriptions == null) {
            mSubscriptions = new CompositeSubscription();
        }

        //recyclerView.setLayoutManager(mgrgridview);
        myAdapter = new MyAdapter();
        myAdapter.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onDownLoad(View view, int position) {

                Log.d(TAG, myAdapter.getStrings().get(position).toString());
                final Dict dict = myAdapter.getStrings().get(position);

                long downloadId = DownloadUtil.newInstance(DictActivity.this).downLoadFile("/dict/doc", "taoge.mdx", dict.getFile().getUrl());
                MdictManager.newInstance(DictActivity.this).addDownloadDictObserver(downloadId,dict);//下载成功后刷新初始化
                downloadIdMap.put(dict.getObjectId(), downloadId);//保存下载id

                //.mdictHome + File.separator + "taoge.mdx", mLocaldict.getFile().getUrl());
                //Toast.makeText(DictActivity.this, "正在下载", Toast.LENGTH_SHORT).show();
            }

        });
        mLocaldict = sharedPreferencesDataSource.getDict();
        //设置适配器
        recyclerView.setAdapter(myAdapter);
        getList();
        DownloadUtilObserver.newInstance().addObserver(observer);
    }

    private Observer observer = new Observer() {
        @Override
        public void update(Observable observable, Object data) {
            DownloadManagerStatus downloadStatus = (DownloadManagerStatus) data;
            //遍历找到当前downloadId的ObjectId
            Iterator iterator = downloadIdMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Long> entry = (Map.Entry<String, Long>) iterator.next();
                if (entry.getValue() == downloadStatus.getDownloadId()) {
                    downloadManagerStatusHashMap.put(entry.getKey(), downloadStatus);
                    break;
                }
            }
            myAdapter.notifyDataSetChanged();
            switch (downloadStatus.getStatus()) {
                case DownloadManager.STATUS_RUNNING:
                    Log.d(TAG, "下载中" + downloadStatus.getDownloadedbyte() + "of" + downloadStatus.getDownloadtotalbyte() + downloadStatus.getProgress());
                    break;
                case DownloadManager.STATUS_PAUSED:
                    Log.d(TAG, "下载暂停");
                    break;
                case DownloadManager.STATUS_FAILED:
                    Log.d(TAG, "下载失败");
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    Log.d(TAG, "下载成功");
                    break;
            }
        }
    };

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
        DownloadUtilObserver.newInstance().deleteObserver(observer);
    }

    void getList() {

        Subscription subscription = repository.getDicts().subscribe(new Subscriber<List<Dict>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof BmobRequestException) {
                    BmobRequestException bmobRequestException = (BmobRequestException) e;
                    Toast.makeText(DictActivity.this, bmobRequestException.getMessage(), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(DictActivity.this, DictActivity.this.getString(R.string.networkerror), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onNext(List<Dict> dicts) {
                if (dicts != null) {
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

        void onDownLoad(View view, int position);
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
            Dict dict = strings.get(position);

            Long donwloadId = downloadIdMap.get(dict.getObjectId());
            if (donwloadId != null) {
                DownloadManagerStatus downloadStatus = downloadManagerStatusHashMap.get(dict.getObjectId());
                if (downloadStatus != null) {
                    switch (downloadStatus.getStatus()) {
                        case DownloadManager.STATUS_RUNNING:
                            holder.btn_download.setText("下载中...");
                            holder.progressBar.setProgress(100);
                            holder.progressBar.setProgress(downloadStatus.getProgress());
                            break;
                        case DownloadManager.STATUS_PAUSED:
                            holder.btn_download.setText("暂停");
                            break;
                        case DownloadManager.STATUS_FAILED:
                            holder.btn_download.setText("失败");
                            break;
                        case DownloadManager.STATUS_SUCCESSFUL:
                            holder.btn_download.setText("完成");
                            holder.progressBar.setProgress(100);
                            holder.btn_download.setOnClickListener(null);
                            break;
                    }
                }
            }else{

                if(mLocaldict != null && mLocaldict.getVersion() == dict.getVersion()){
                    holder.btn_download.setText("已下载");
                    holder.btn_download.setOnClickListener(null);
                }else if(mLocaldict != null && mLocaldict.getVersion() < dict.getVersion()){
                    holder.btn_download.setText("有更新");
                    holder.btn_download.setEnabled(true);
                }else {
                    holder.btn_download.setText("下载");
                    holder.btn_download.setEnabled(true);
                }
            }

            holder.textView.setText(strings.get(position).getName());
        }

        @Override
        public int getItemCount() {
            return strings.size();
        }


        //自定义的ViewHolder,减少findViewById调用次数
        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView textView;
            AppCompatButton btn_download;
            ProgressBar progressBar;

            public ViewHolder(final View itemView) {
                super(itemView);

                itemView.setOnClickListener(this);

                textView = (TextView) itemView.findViewById(R.id.name);
                btn_download = (AppCompatButton) itemView.findViewById(R.id.btn_download);
                progressBar = (ProgressBar) itemView.findViewById(R.id.progress);
                btn_download.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {

                if (onItemClickListener != null) {

                    switch (v.getId()) {
                        case R.id.item:
                            onItemClickListener.onItemClick(itemView, getAdapterPosition());
                            break;
                        case R.id.btn_download:
                            onItemClickListener.onDownLoad(itemView, getAdapterPosition());
                            break;
                    }
                }
            }
        }
    }
}


