package com.example.randomalarm.alarm;

import android.content.Context;

/**
 * author : Mzz
 * date : 2019 2019/5/10 21:51
 * description :
 */
public class AlarmNotification {
    private NotificationUtils notificationUtils;

    public AlarmNotification(Context context) {
        notificationUtils = new NotificationUtils(context);
    }

    public void show(String content) {
        notificationUtils.sendNotification("闹钟提醒", content);
    }

    private void cancel() {
    }
}
