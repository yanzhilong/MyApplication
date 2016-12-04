package com.englishlearn.myapplication.observer;

/**
 * Created by yanzl on 16-12-4.
 */

public class DownloadObserver extends java.util.Observable{

    private static DownloadObserver INSTANCE;

    public static synchronized DownloadObserver newInstance() {
        if(INSTANCE == null){
            INSTANCE = new DownloadObserver();
        }
        return INSTANCE;
    }

    @Override
    public void notifyObservers(Object data) {
        this.setChanged();
        super.notifyObservers(data);

    }
}
