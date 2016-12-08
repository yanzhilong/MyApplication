package com.englishlearn.myapplication.core;

import android.net.Uri;

import java.io.Serializable;

/**
 * Created by yanzl on 16-12-4.
 * DownloadManager的状态　
 */

public class DownloadManagerStatus implements Serializable,Cloneable {

    private long downloadId;//下载id
    private int downloadedbyte;//已经下载
    private int downloadtotalbyte;//总的大小
    private int status;//下载状态
    private int progress;//下载进度
    private Uri fileUri;//下载的文件uri

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
}
