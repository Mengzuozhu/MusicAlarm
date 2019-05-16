package com.example.randomalarm.common;

import android.app.Service;
import android.content.Context;
import android.os.Vibrator;

/**
 * author : Mzz
 * date : 2019 2019/5/16 19:13
 * description :
 */
public class VibrateHandler {
    private Vibrator vibrator;

    public VibrateHandler(Context context) {
        vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
    }

    public void vibrate(long[] vibratePattern,int repeat) {
        if ( vibrator.hasVibrator()) {
            cancel();
            vibrator.vibrate(vibratePattern, repeat);
        }
    }

    public void cancel() {
        vibrator.cancel();
    }
}
