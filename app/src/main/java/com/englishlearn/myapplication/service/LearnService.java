package com.englishlearn.myapplication.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.englishlearn.myapplication.core.ClipboardMonitor;
import com.englishlearn.myapplication.receiver.KeepAliveReceiver;

/**
 * Created by yanzl on 17-1-2.
 */

public class LearnService extends Service {


    private PendingIntent mkeepAlivePendingIntent;
    private BroadcastReceiver mKeepAliveReceiver = new KeepAliveReceiver();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ClipboardMonitor.newInstance(this).init();


        IntentFilter lFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        lFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mKeepAliveReceiver, lFilter);
        //保活
        Intent intent = new Intent(this, KeepAliveReceiver.class);
        mkeepAlivePendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        ((AlarmManager) this.getSystemService(Context.ALARM_SERVICE)).setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP
                , SystemClock.elapsedRealtime() + 600000
                , 600000
                , mkeepAlivePendingIntent);
    }



}
