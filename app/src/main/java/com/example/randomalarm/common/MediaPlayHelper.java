package com.example.randomalarm.common;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

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

    public MediaPlayHelper(Context context, Uri uri) {
        player = MediaPlayer.create(context, uri);
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

    /**
     * 设置重复播放
     */
    public void setLoopPlay() {
        setOnCompletionListener(mp -> {
            if (player != null) {
                start();
            }
        });
    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener listener) {
        player.setOnCompletionListener(listener);
    }
}
