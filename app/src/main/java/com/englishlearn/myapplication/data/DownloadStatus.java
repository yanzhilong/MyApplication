package com.englishlearn.myapplication.data;

import java.io.Serializable;

/**
 * Created by yanzl on 16-12-4.
 */

public class DownloadStatus implements Serializable,Cloneable {

    private String url;
    private long size;//总大小
    private long currentsize;//当前大小
    private String sizeStr;
    private String currentsizestr;//
    private boolean success;
    private boolean exception;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isException() {
        return exception;
    }

    public void setException(boolean exception) {
        this.exception = exception;
    }

    private int percent;//百分比

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getCurrentsize() {
        return currentsize;
    }

    public void setCurrentsize(long currentsize) {
        this.currentsize = currentsize;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public String getSizeStr() {
        return sizeStr;
    }

    public void setSizeStr(String sizeStr) {
        this.sizeStr = sizeStr;
    }

    public String getCurrentsizestr() {
        return currentsizestr;
    }

    public void setCurrentsizestr(String currentsizestr) {
        this.currentsizestr = currentsizestr;
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
