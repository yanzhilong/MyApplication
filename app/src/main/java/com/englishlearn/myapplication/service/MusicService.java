package com.englishlearn.myapplication.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;

import java.io.IOException;

/**
 * Created by yanzl on 16-10-23.
 */

public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    private MediaPlayer mediaPlayer;
    private final IBinder musicBind = new MusicBinder();

    @Override
    public void onCreate() {
        super.onCreate();

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);

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
        mediaPlayer.stop();
        mediaPlayer.release();
        return false;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    ///准备完毕后回调
    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();
    }

    //播放完毕后回调
    @Override
    public void onCompletion(MediaPlayer mp) {
        mediaPlayer.reset();//播放另一文件前设置为初始状态
    }

    //播放出错回调
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mediaPlayer.reset();//恢复初始状态
        return false;
    }

    //播放本地
    public void playFile(String filePath) throws IOException {
        if(mediaPlayer.isPlaying()){
            return;
        }
        mediaPlayer.setDataSource(filePath);//设置播放的数据源
        mediaPlayer.prepareAsync();//准备开始播放
    }

    public void playUrl(String url) throws IOException {
        if(mediaPlayer.isPlaying()){
            return;
        }
        mediaPlayer.setDataSource(url);//参数可以直接是网络路径
        mediaPlayer.prepareAsync();//异步准备
    }

    /**
     * 暂停
     */
    public void pause() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {//只有播放器已初始化并且正在播放才可暂停
                mediaPlayer.pause();
            }
        }
    }

    //恢复播放
    public void replay(){
        if (mediaPlayer != null) {
            if (!mediaPlayer.isPlaying()) {//只有播放器已初始化并且正在播放才可暂停
                mediaPlayer.start();
            }
        }
    }

    /**
     * 停止
     */
    public void stop() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.reset();
    }
}
