package com.englishlearn.myapplication.core;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.englishlearn.myapplication.observer.DownloadManagerObserver;

import java.io.File;

/**
 * Created by yanzl on 16-12-4.
 * 下载管理类
 */

public class DownloadUtil {

    private final static String TAG = DownloadUtil.class.getSimpleName();


    private static DownloadUtil downloadUtil;

    private Context context;

    public DownloadUtil(Context context) {

        this.context = context;
    }

    public static synchronized DownloadUtil newInstance(Context context) {
        if(downloadUtil == null){
            downloadUtil = new DownloadUtil(context);
        }
        return downloadUtil;
    }

    public long downLoadFile(String dirType, String subPath, final String url){

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        //如果存在就先删除
        final File dirtype = context.getExternalFilesDir(dirType);
        Uri.Builder builder = Uri.fromFile(dirtype).buildUpon();
        builder = builder.appendEncodedPath(subPath);
        File file = new File(builder.build().getPath());
        if(file.exists()){
            file.delete();
        }

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
        request.setDestinationInExternalFilesDir(context, dirType, subPath);
        /**将下载请求放入队列， return下载任务的ID*/
        long downloadId = downloadManager.enqueue(request);

        Log.d(TAG,"下载Id:" + downloadId);

        //在执行下载前注册内容监听者
        context.getContentResolver().registerContentObserver(Uri.parse("content://downloads/"), true, DownloadManagerObserver.newInstance(new Handler(),context,downloadId));
        return downloadId;
    }

}
