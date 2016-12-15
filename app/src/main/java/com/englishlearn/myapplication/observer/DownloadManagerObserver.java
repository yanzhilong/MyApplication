package com.englishlearn.myapplication.observer;

import android.app.DownloadManager;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.util.Log;

import com.englishlearn.myapplication.core.DownloadStatus;
import com.englishlearn.myapplication.core.DownloadUtil;

import java.util.List;
import java.util.Observable;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by yanzl on 16-12-8.
 */

public class DownloadManagerObserver extends ContentObserver {

    private static DownloadManagerObserver INSTANCE;
    private final Observable observable;
    private Context context;

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
    }


    @Override
    public void onChange(boolean selfChange) {
        // 每当/data/data/com.android.providers.download/database/database.db变化后，触发onCHANGE，开始具体查询
        super.onChange(selfChange);
        Log.d(TAG,"onChange selfChange:" + selfChange + observable);
        if(observable != null){
            List<DownloadStatus> downloadStatusList = DownloadUtil.newInstance(context).getDownloadList();
            observable.notifyObservers(downloadStatusList);
        }
    }




}
