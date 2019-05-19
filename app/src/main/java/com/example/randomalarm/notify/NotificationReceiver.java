package com.example.randomalarm.notify;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.randomalarm.alarm.AlarmConstant;
import com.example.randomalarm.common.DateHelper;

import java.util.Calendar;

/**
 * author : Mzz
 * date : 2019 2019/5/19 15:44
 * description :
 */
public class NotificationReceiver extends BroadcastReceiver {

    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        long alarmId = intent.getLongExtra(AlarmConstant.ALARM_ID, -1);
        if (alarmId == -1) {
            return;
        }
        setNotification();
    }

    private void setNotification() {
        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(nextCalendar.getTimeInMillis());
//        calendar.add(Calendar.HOUR_OF_DAY, -1);
//        calendar.add(Calendar.SECOND, 15);
//        calendar.add(Calendar.MINUTE, 1);
        Log.w("Notification", DateHelper.formatDateAndHHmm(calendar));
        NotificationUtils notificationUtils = new NotificationUtils(context);
        notificationUtils.sendNotification("音乐闹钟提醒", "下一个闹钟将于1小时内响起");
//        if (calendar.compareTo(DateHelper.getNowTime()) > 0) {
//            NotificationUtils notificationUtils = new NotificationUtils(context);
//            notificationUtils.sendNotification("音乐闹钟提醒", "下一个闹钟将于1小时内响起");
//        }
    }

}
