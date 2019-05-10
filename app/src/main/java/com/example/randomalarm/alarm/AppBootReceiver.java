package com.example.randomalarm.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * author : Mzz
 * date : 2019 2019/5/10 21:05
 * description :
 */
public class AppBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent service = new Intent(context, AlarmInitService.class);
            context.startService(service);
        }
    }
}
