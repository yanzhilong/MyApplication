package com.englishlearn.myapplication.observer;

import android.app.DownloadManager;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.util.Log;

import com.englishlearn.myapplication.MyApplication;
import com.englishlearn.myapplication.config.ApplicationConfig;
import com.englishlearn.myapplication.core.DownloadStatus;
import com.englishlearn.myapplication.data.source.Repository;

import java.util.List;
import java.util.Observable;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by yanzl on 16-12-8.
 */

public class DownloadManagerObserver extends ContentObserver {

    private final static String TAG = DownloadManagerObserver.class.getSimpleName();
    private static DownloadManagerObserver INSTANCE;
    private final Observable observable;
    private Context context;
    private long lastTime = 0;

    @Inject
    Repository repository;
    public static synchronized DownloadManagerObserver newInstance(Handler handler, Context mContext, Observable observable) {

        if(INSTANCE == null){
            INSTANCE = new DownloadManagerObserver(handler,mContext,observable);
        }
        return INSTANCE;
    }

    private DownloadManager mDownloadManager;


    private DownloadManagerObserver(Handler handler, Context mContext,Observable observable) {
        super(handler);
        this.context = context;
        mDownloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        this.observable = observable;
        MyApplication.instance.getAppComponent().inject(this);
    }


    @Override
    public void onChange(boolean selfChange) {
        // 每当/data/data/com.android.providers.download/database/database.db变化后，触发onCHANGE，开始具体查询
        super.onChange(selfChange);
        if(System.currentTimeMillis() - lastTime < ApplicationConfig.DOWNLOADREFRESH){
            return;
        }
        lastTime = System.currentTimeMillis();
        Log.d(TAG,"onChange selfChange:" + Thread.currentThread().getName());
        if(observable != null){
            //List<DownloadStatus> downloadStatusList = DownloadUtil.newInstance(context).getAllDownloadList();
            repository.getDownloadList().subscribe(new Subscriber<List<DownloadStatus>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(List<DownloadStatus> downloadStatuses) {
                    Log.d(TAG,"onNext:" + Thread.currentThread().getName());
                    observable.notifyObservers(downloadStatuses);
                }
            });

        }
    }




}
