package com.englishlearn.myapplication.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.englishlearn.myapplication.core.ClipboardMonitor;

/**
 * Created by yanzl on 17-1-2.
 */

public class LearnService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ClipboardMonitor.newInstance(this).init();
    }



}
