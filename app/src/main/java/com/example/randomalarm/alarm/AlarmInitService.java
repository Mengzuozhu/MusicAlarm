package com.example.randomalarm.alarm;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * 闹钟初始化服务
 * author : Mzz
 * date : 2019 2019/5/3 21:14
 * description :
 */
public class AlarmInitService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(() -> {
            AlarmMangerClass alarmHandler =new AlarmMangerClass(this);
            alarmHandler.intiAllAlarms();
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    private void showAlarm() {
        Intent intent = new Intent(this, AlarmRemindActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }
}
