package com.englishlearn.myapplication.core;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.englishlearn.myapplication.observer.DownloadManagerObserver;
import com.englishlearn.myapplication.observer.DownloadUtilObserver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanzl on 16-12-4.
 * 下载管理类
 */

public class DownloadUtil {

    private final static String TAG = DownloadUtil.class.getSimpleName();


    private static DownloadUtil downloadUtil;

    private Context context;
    private DownloadManager downloadManager;

    public DownloadUtil(Context context) {
        this.context = context;
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    public static synchronized DownloadUtil newInstance(Context context) {
        if (downloadUtil == null) {
            downloadUtil = new DownloadUtil(context);
        }
        return downloadUtil;
    }

    /**
     * 下载一个文件
     *
     * @param dirType
     * @param subPath
     * @param url
     * @return
     */
    public long downLoadFile(String dirType, String subPath, final String url, boolean isExternal) {

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        /**设置用于下载时的网络状态*/
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        /**设置通知栏是否可见*/
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        /**设置漫游状态下是否可以下载*/
        request.setAllowedOverRoaming(false);
        /**如果我们希望下载的文件可以被系统的Downloads应用扫描到并管理，
         我们需要调用Request对象的setVisibleInDownloadsUi方法，传递参数true.*/
        request.setVisibleInDownloadsUi(true);
        /**设置文件保存路径*/
        if (isExternal) {
            request.setDestinationInExternalPublicDir(dirType, subPath);
        } else {
            request.setDestinationInExternalFilesDir(context, dirType, subPath);
        }
        /**将下载请求放入队列， return下载任务的ID*/
        long downloadId = downloadManager.enqueue(request);

        Log.d(TAG, "下载Id:" + downloadId);

        //在执行下载前注册内容监听者
        context.getContentResolver().registerContentObserver(Uri.parse("content://downloads/"), true, DownloadManagerObserver.newInstance(new Handler(), context, DownloadUtilObserver.newInstance()));
        return downloadId;
    }



    /**
     * 下载一个文件
     *
     * @param dirType
     * @param subPath
     * @param url
     * @param isOverite 复盖原有文件，删除原来的链接
     * @return
     */
    public long downLoadFile(String dirType, String subPath, final String url, boolean isExternal, boolean isOverite) {

        if(isOverite){
            //如果存在就先删除
            final File dirtype = context.getExternalFilesDir(dirType);
            Uri.Builder builder = Uri.fromFile(dirtype).buildUpon();
            builder = builder.appendEncodedPath(subPath);
            File file = new File(builder.build().getPath());
            if (file.exists()) {
                file.delete();
            }
            //判断是否在下载列表中，有在下载列表中就先删除
            List<DownloadStatus> downloadStatusList = getAllDownloadList();
            for (DownloadStatus downloadStatus : downloadStatusList) {
                if (downloadStatus.getUrl().equals(url)) {
                    deleteDownload(downloadStatus.getDownloadId());
                }
            }

            return downLoadFile(dirType,subPath,url,isExternal);
        }else{
            return downLoadFile(dirType,subPath,url,isExternal);
        }

    }

    /**
     * 删除下载
     *
     * @param downloadId
     */
    public void deleteDownload(final long downloadId) {
        downloadManager.remove(downloadId);
    }


    /**
     * 获得全部的下载列表
     *
     * @return
     */
    public List<DownloadStatus> getAllDownloadList() {

        List<DownloadStatus> downloadStatusList = new ArrayList<>();
        DownloadManager.Query query = new DownloadManager.Query();
        Cursor cursor = downloadManager.query(query);
        while (cursor.moveToNext()) {

            long time = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_LAST_MODIFIED_TIMESTAMP));
//            Log.d(TAG, "time:" + System.currentTimeMillis());
//            Log.d(TAG, "time:" + time);
            int reason = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_REASON));
            int downId = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_ID));
            int bytes_downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
            int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            String url = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_URI));//下载的url
            int progress = (int) ((float) bytes_downloaded / (float) bytes_total * 100);
            Log.d(TAG, bytes_downloaded + "/" + bytes_total + "--" + progress + "uri" + downloadManager.getUriForDownloadedFile(downId));
            DownloadStatus downloadStatus = new DownloadStatus();
            downloadStatus.setDownloadId(downId);
            downloadStatus.setDownloadedbyte(bytes_downloaded);
            downloadStatus.setStatus(status);
            downloadStatus.setReason(reason);
            downloadStatus.setDownloadtotalbyte(bytes_total);
            downloadStatus.setFileUri(downloadManager.getUriForDownloadedFile(downId));
            downloadStatus.setProgress(progress);
            downloadStatus.setUrl(url);
            downloadStatusList.add(downloadStatus);
        }
        cursor.close();
        return downloadStatusList;
    }

}
