package com.tomandjerry.tomandjerryv2.Utilities;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyMediaPlayer {
    private Context context;
    private HashMap<Integer, MediaPlayer> mediaPlayerMap;
    private ExecutorService executorService;

    public MyMediaPlayer(Context context) {
        this.context = context;
        this.mediaPlayerMap = new HashMap<>();
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public void playSound(int resId, float leftVolume, float rightVolume) {
        executorService.execute(() -> {
            MediaPlayer mediaPlayer = mediaPlayerMap.get(resId);

            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(context, resId);
                if (mediaPlayer != null) {
                    mediaPlayer.setVolume(leftVolume, rightVolume);
                    mediaPlayer.setOnCompletionListener(mp -> mp.seekTo(0));
                    mediaPlayerMap.put(resId, mediaPlayer);
                    mediaPlayer.start();
                    Log.d("MyMediaPlayer", "New sound started: " + resId);
                } else {
                    Log.e("MyMediaPlayer", "Failed to create MediaPlayer for resource: " + resId);
                }
            } else {
                mediaPlayer.setVolume(leftVolume, rightVolume);
                mediaPlayer.start();
                Log.d("MyMediaPlayer", "Existing sound restarted: " + resId);
            }
        });
    }

    public void stopAllSounds() {
        executorService.execute(() -> {
            for (MediaPlayer mediaPlayer : mediaPlayerMap.values()) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
            }
            mediaPlayerMap.clear();
            Log.d("MyMediaPlayer", "All sounds stopped and released");
        });
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
