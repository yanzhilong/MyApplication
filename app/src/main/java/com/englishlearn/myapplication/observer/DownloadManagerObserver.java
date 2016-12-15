package com.englishlearn.myapplication.observer;

import android.app.DownloadManager;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;

import com.englishlearn.myapplication.core.DownloadStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanzl on 16-12-8.
 */

public class DownloadManagerObserver extends ContentObserver {

    public static synchronized DownloadManagerObserver newInstance(Handler handler, Context mContext) {
        return new DownloadManagerObserver(handler,mContext);
    }

    private DownloadManager mDownloadManager;


    private DownloadManagerObserver(Handler handler, Context mContext) {
        super(handler);
        mDownloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
    }


    @Override
    public void onChange(boolean selfChange) {
        // 每当/data/data/com.android.providers.download/database/database.db变化后，触发onCHANGE，开始具体查询
        super.onChange(selfChange);
        getDownloadList();
    }


    public List<DownloadStatus> getDownloadList(){

        List<DownloadStatus> downloadStatusList = new ArrayList<>();
        DownloadManager.Query query = new DownloadManager.Query();
        Cursor cursor = mDownloadManager.query(query);
        while (cursor.moveToNext()){
            int downId = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_ID));
            int bytes_downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
            int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            String url = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_URI));//下载的url
            int progress = ((bytes_downloaded * 100) / bytes_total);
            DownloadStatus downloadStatus = new DownloadStatus();
            downloadStatus.setDownloadId(downId);
            downloadStatus.setDownloadedbyte(bytes_downloaded);
            downloadStatus.setStatus(status);
            downloadStatus.setDownloadtotalbyte(bytes_total);
            downloadStatus.setFileUri(mDownloadManager.getUriForDownloadedFile(downId));
            downloadStatus.setProgress(progress);
            downloadStatus.setUrl(url);
            downloadStatusList.add(downloadStatus);
        }
        cursor.close();

        return downloadStatusList;
    }
}
