package com.example.randomalarm.alarm;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.randomalarm.MainActivity;

/**
 * author : Mzz
 * date : 2019 2019/5/4 21:11
 * description :
 */
public class ForegroundService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification.Builder builder = new Notification.Builder(this.getApplicationContext());
        Intent nfIntent = new Intent(this, MainActivity.class);
        builder.setContentIntent(PendingIntent.getActivity(this, 0, nfIntent, 0))
                //设置服务标题
                .setContentTitle("服务123")
                //设置状态栏小图标
                //设置服务内容
                .setContentText("服务正在运行")
                //设置通知时间
                .setWhen(System.currentTimeMillis());
        Notification notification = null;
        notification = builder.build();
        startForeground(110, notification);
        return super.onStartCommand(intent, flags, startId);
    }
}
