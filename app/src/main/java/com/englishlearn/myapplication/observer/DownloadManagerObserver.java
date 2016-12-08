package com.englishlearn.myapplication.observer;

import android.app.DownloadManager;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;

import com.englishlearn.myapplication.core.DownloadManagerStatus;

/**
 * Created by yanzl on 16-12-8.
 */

public class DownloadManagerObserver extends ContentObserver {

    public static synchronized DownloadManagerObserver newInstance(Handler handler, Context mContext, long downId) {
        return new DownloadManagerObserver(handler,mContext,downId);
    }

    private DownloadManager mDownloadManager;
    private DownloadManager.Query query;
    private long downId;
    private Cursor cursor;

    private DownloadManagerObserver(Handler handler, Context mContext, long downId) {
        super(handler);
        this.downId = downId;
        mDownloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        query = new DownloadManager.Query().setFilterById(downId);
    }


    @Override
    public void onChange(boolean selfChange) {
        // 每当/data/data/com.android.providers.download/database/database.db变化后，触发onCHANGE，开始具体查询
        super.onChange(selfChange);
        //
        cursor = mDownloadManager.query(query);
        cursor.moveToFirst();
        int bytes_downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
        int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
        int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
        switch (status) {
            case DownloadManager.STATUS_RUNNING:

                break;
            case DownloadManager.STATUS_PAUSED:

                break;
            case DownloadManager.STATUS_FAILED:

                break;
            case DownloadManager.STATUS_SUCCESSFUL:

                break;
        }
        int progress = ((bytes_downloaded * 100) / bytes_total);
        cursor.close();

        DownloadManagerStatus downloadManagerStatus = new DownloadManagerStatus();
        downloadManagerStatus.setDownloadedbyte(bytes_downloaded);
        downloadManagerStatus.setDownloadId(downId);
        downloadManagerStatus.setStatus(status);
        downloadManagerStatus.setDownloadtotalbyte(bytes_total);
        downloadManagerStatus.setFileUri(mDownloadManager.getUriForDownloadedFile(downId));
        downloadManagerStatus.setProgress(progress);
        DownloadUtilObserver.newInstance().notifyObservers(downloadManagerStatus);
    }
}
