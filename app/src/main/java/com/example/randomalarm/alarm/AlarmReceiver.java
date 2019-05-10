package com.example.randomalarm.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.randomalarm.common.DateHelper;
import com.example.randomalarm.common.ViewerHelper;

import java.util.Calendar;

/**
 * Created by Jay on 2015/10/25 0025.
 */
public class AlarmReceiver extends BroadcastReceiver {

    public static final String ACTION_ALARM = "com.example.randomalarm.ALARM_BROADCAST";
    static int i = 0;
    static int INTERVAL = 10000;

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if (!ACTION_ALARM.equals(action)) return;

//        AlarmHandlerClass.judgeAlarm(context);
//        Intent intent1 = new Intent(context, AlarmCheckService.class);
        Intent intent1 = new Intent(AlarmReceiver.ACTION_ALARM);
        ComponentName componentName = new ComponentName(context, AlarmReceiver.class);
        intent1.setComponent(componentName);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent1, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmHandlerClass.setAlarmTime(alarmManager, sender, getNextTime());
        Log.e("接收器", "接收到" + i++);
    }

    public void showNextAlarm(AlarmManager alarmManager) {
        AlarmManager.AlarmClockInfo nextAlarmClock = alarmManager.getNextAlarmClock();
        if (nextAlarmClock == null) return;

        long triggerTime = nextAlarmClock.getTriggerTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(triggerTime);
        String format = DateHelper.format(calendar.getTime());
        Log.e("下一次闹钟：", format);

    }

    public long getNextTime() {
//        Calendar calendar = Calendar.getInstance();
//        int second = calendar.get(Calendar.SECOND);
//        int delay = 60 - second;
//        calendar.add(Calendar.SECOND, delay);
//        return calendar.getTimeInMillis();
        return System.currentTimeMillis() + INTERVAL;
    }
}
