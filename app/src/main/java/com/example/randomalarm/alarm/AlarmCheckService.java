package com.example.randomalarm.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.example.randomalarm.common.ViewerHelper;

import java.util.Calendar;

/**
 * author : Mzz
 * date : 2019 2019/5/3 21:14
 * description :
 */
public class AlarmCheckService extends Service {

    static int CLOCK_ID = 1;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //这里开辟一条线程,用来执行具体的逻辑操作:
//        new Thread(() -> {
//            showAlarm();
//        }).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        int anHour = 2 * 1000;
//        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Calendar calendar = Calendar.getInstance();
        int second = calendar.get(Calendar.SECOND);
//        int delay = 60 - second;
        int delay = 3;
        calendar.add(Calendar.SECOND, delay);
        Intent intent1 = new Intent(AlarmReceiver.ACTION_ALARM);
        ComponentName componentName = new ComponentName(this, AlarmReceiver.class);
        intent1.setComponent(componentName);
        intent1.putExtra("1","2");
        Log.e("设置服务", "编号：" + CLOCK_ID++);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
        return super.onStartCommand(intent, flags, startId);
    }

    private void showAlarm() {
        Intent intent = new Intent(this, AlarmActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        this.startActivity(intent);
    }
}
