package com.englishlearn.myapplication.data.source.remote.bmob;

/**
 * Created by yanzl on 16-9-20.
 */
public class BmobRequestException extends Throwable {

    private Object object;
    private BmobDefaultError bmobDefaultError;
    public BmobRequestException() {
        super();
    }

    public BmobRequestException(String detailMessage) {
        super(detailMessage);
    }

    public BmobDefaultError getBmobDefaultError() {
        return bmobDefaultError;
    }

    public BmobRequestException(BmobDefaultError bmobDefaultError) {
        this.bmobDefaultError = bmobDefaultError;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
