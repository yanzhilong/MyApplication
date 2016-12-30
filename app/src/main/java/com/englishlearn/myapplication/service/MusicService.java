package com.englishlearn.myapplication.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.IOException;

/**
 * Created by yanzl on 16-10-23.
 */

public class MusicService extends Service {

    private final IBinder musicBind = new MusicBinder();
    private Playback playback;

    @Override
    public void onCreate() {
        super.onCreate();

        playback = Playback.newInstance(getApplication());

    }

    //binder
    public class MusicBinder extends Binder {

        public MusicService getService() {
            return MusicService.this;
        }
    }

    //release resources when unbind
    @Override
    public boolean onUnbind(Intent intent){
        return false;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    public void playUrl(String url) throws IOException {
        playback.play(url);
        /*if(mediaPlayer.isPlaying()){
            return;
        }
        mediaPlayer.reset();//播放另一文件前设置为初始状态
        mediaPlayer.setDataSource(url);//参数可以直接是网络路径
        mediaPlayer.prepare();//异步准备
        mediaPlayer.start();*/
    }
}
