package com.englishlearn.myapplication.util;

import android.media.MediaPlayer;

import java.io.File;

/**
 * Created by yanzl on 16-10-23.
 */

public class MediaPlayerControl {

    private static MediaPlayerControl mediaPlayerControl;
    private MediaPlayer mediaPlayer;

    public static synchronized MediaPlayerControl newInstance(){
        if(mediaPlayerControl == null){
            mediaPlayerControl = new MediaPlayerControl();
        }
        return mediaPlayerControl;
    }

    public MediaPlayerControl() {
        mediaPlayer = new MediaPlayer();
    }

    public void play(File file){

    }
}
