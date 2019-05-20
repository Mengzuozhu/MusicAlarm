package com.example.randomalarm.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.randomalarm.common.EventBusHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

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
            EventBus.getDefault().post(Calendar.getInstance());
        }
    }
}
