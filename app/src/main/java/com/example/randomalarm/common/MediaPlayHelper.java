package com.example.randomalarm.common;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * author : Mzz
 * date : 2019 2019/5/13 10:11
 * description :
 */
public class MediaPlayHelper {
    private MediaPlayer player;

    public MediaPlayHelper(Context context, int resid) {
        player = MediaPlayer.create(context, resid);
    }

    public void start() {
        player.start();
    }

    public void release() {
        if (player != null) {
            if (player.isPlaying()) {
                player.stop();
            }
            player.release();
            player = null;
        }
    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener listener) {
        player.setOnCompletionListener(listener);
    }
}
