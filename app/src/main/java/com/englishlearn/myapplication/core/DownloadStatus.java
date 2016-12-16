package com.englishlearn.myapplication.core;

import android.app.DownloadManager;
import android.net.Uri;

import java.io.File;
import java.io.Serializable;

/**
 * Created by yanzl on 16-12-4.
 * DownloadManager的状态　
 */

public class DownloadStatus implements Serializable,Cloneable {

    private long downloadId;//下载id
    private int downloadedbyte;//已经下载
    private int downloadtotalbyte;//总的大小
    private int status;//下载状态
    private int reason;//错误原因
    private int progress;//下载进度
    private Uri fileUri;//下载的文件uri
    private String url;//下载链接

    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(long downloadId) {
        this.downloadId = downloadId;
    }

    public int getDownloadedbyte() {
        return downloadedbyte;
    }

    public void setDownloadedbyte(int downloadedbyte) {
        this.downloadedbyte = downloadedbyte;
    }

    public int getDownloadtotalbyte() {
        return downloadtotalbyte;
    }

    public void setDownloadtotalbyte(int downloadtotalbyte) {
        this.downloadtotalbyte = downloadtotalbyte;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public Uri getFileUri() {
        return fileUri;
    }

    public void setFileUri(Uri fileUri) {
        this.fileUri = fileUri;
    }

    /**
     * 判断文件是否存在
     * @return
     */
    public boolean isFileExists(){
        if(getFileUri() != null){
            File file = new File(getFileUri().getPath());
            return file.exists();
        }
        return false;
    }

    @Override
    public Object clone(){

        Object o = null;
        try {
            o = super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return o;
    }

    public String getErrorReason(){
        switch (reason){
            case DownloadManager.ERROR_CANNOT_RESUME:
            case DownloadManager.ERROR_UNKNOWN:

                return "不能继续，未知错误";
            case DownloadManager.ERROR_DEVICE_NOT_FOUND:

                return "外部存储未找到";
            case DownloadManager.ERROR_FILE_ALREADY_EXISTS:

                return "文件已经存在了";
            case DownloadManager.ERROR_FILE_ERROR:

                return "存储介质故障";
            case DownloadManager.ERROR_HTTP_DATA_ERROR:

                return "传输错误";
            case DownloadManager.ERROR_INSUFFICIENT_SPACE:

                return "空间不足";
            case DownloadManager.ERROR_TOO_MANY_REDIRECTS:

                return "HTTP太多重定向";
            case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:

                return "网络故障";
        }
        return "未知错误";
    }
}
