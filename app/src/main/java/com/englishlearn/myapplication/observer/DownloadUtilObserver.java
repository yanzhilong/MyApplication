package com.englishlearn.myapplication.observer;

/**
 * Created by yanzl on 16-12-4.
 */

public class DownloadUtilObserver extends java.util.Observable{

    private static DownloadUtilObserver INSTANCE;

    public static synchronized DownloadUtilObserver newInstance() {
        if(INSTANCE == null){
            INSTANCE = new DownloadUtilObserver();
        }
        return INSTANCE;
    }

    @Override
    public void notifyObservers(Object data) {
        this.setChanged();
        super.notifyObservers(data);

    }
}
