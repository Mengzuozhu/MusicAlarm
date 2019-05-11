package com.example.randomalarm.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.randomalarm.common.ViewerHelper;

/**
 * author : Mzz
 * date : 2019 2019/5/10 21:05
 * description :
 */
public class AppBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            ViewerHelper.showToast(context,"重启");
            Intent service = new Intent(context, AlarmInitService.class);
            context.startService(service);
        }
    }
}
