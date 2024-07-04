package com.tomandjerry.tomandjerryv2.Utilities;

import android.content.Context;
import android.media.MediaPlayer;

public class MyBackgroundMusic {

    private static Context context;
    private static volatile MyBackgroundMusic instance;
    private MediaPlayer mediaPlayer;
    private int RES_ID;

    private MyBackgroundMusic(Context context) {
        this.context = context;
    }

    public static MyBackgroundMusic getInstance() {
        return instance;
    }

    public static MyBackgroundMusic init(Context context) {
        if (instance == null) {
            synchronized (MyBackgroundMusic.class) {
                if (instance == null) {
                    instance = new MyBackgroundMusic(context.getApplicationContext());
                }
            }
        }
        return getInstance();
    }

    public void setResourceId(int RES_ID) {
        this.RES_ID = RES_ID;
        initMediaPlayer();
    }

    private void initMediaPlayer() {
        if (mediaPlayer != null) {
            release();
        }
        mediaPlayer = MediaPlayer.create(context, RES_ID);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(0.1f, 0.1f);
    }

    public void playMusic() {
        if (mediaPlayer == null || mediaPlayer.isPlaying()) {
            initMediaPlayer();
        }

        try {
            mediaPlayer.start();
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }
    }

    public void pauseMusic() {
        if (mediaPlayer == null || !mediaPlayer.isPlaying()) {
            return;
        }

        try {
            mediaPlayer.pause();
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }
    }

    public void stopMusic() {
        if (mediaPlayer == null || !mediaPlayer.isPlaying()) {
            return;
        }

        try {
            mediaPlayer.stop();
            release();
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }
    }

    public void release() {
        if (mediaPlayer == null) {
            return;
        }

        try {
            mediaPlayer.release();
            mediaPlayer = null;
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }
    }

    public boolean isMusicPlaying() {
        if (mediaPlayer != null) {
            try {
                return mediaPlayer.isPlaying();
            } catch (IllegalStateException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }


}