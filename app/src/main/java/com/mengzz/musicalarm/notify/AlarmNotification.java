package com.mengzz.musicalarm.notify;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

/**
 * author : Mzz
 * date : 2019 2019/5/10 21:51
 * description :
 */
public class AlarmNotification extends BroadcastReceiver {

    public final static String BUTTON_NAME = "BUTTON_NAME";
    public final static String CLOSE_BUTTON = "CLOSE_BUTTON";
    public final static String REMIND_BUTTON = "REMIND_BUTTON";

    @Override
    public void onReceive(Context context, Intent intent) {
        String buttonName = intent.getStringExtra(BUTTON_NAME);
        if (TextUtils.isEmpty(buttonName)) {
            return;
        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

        if (CLOSE_BUTTON.equals(buttonName)) {

        } else if (REMIND_BUTTON.equals(buttonName)) {

        }
        Log.w("BUTTON_NAME", buttonName);

    }
}
