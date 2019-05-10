package com.example.randomalarm.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * author : Mzz
 * date : 2019 2019/5/9 14:49
 * description :
 */
public class TimeTickReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_TIME_TICK.equals(action)) {
            Log.i("分钟接收器", action);
            // to do
        }
    }
}
