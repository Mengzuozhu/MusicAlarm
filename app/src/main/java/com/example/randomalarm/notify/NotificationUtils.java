package com.example.randomalarm.notify;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.example.randomalarm.R;

/**
 * Created by LaoZhao on 2017/11/19.
 */

public class NotificationUtils extends ContextWrapper {

    public static final String channelID = "channel_1";
    public static final String channelName = "channel_name_1";
    private NotificationManager manager;

    public NotificationUtils(Context context) {
        super(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
        getManager().createNotificationChannel(channel);
    }

    private NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return manager;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getChannelNotification(String title, String content) {
        RemoteViews remoteViews = getRemoteViews(title, content);
        return new Notification.Builder(getApplicationContext(), channelID)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.app_alarm)
                .setCustomContentView(remoteViews);
    }

    public RemoteViews getRemoteViews(String title, String content) {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.alarm_notification);
        remoteViews.setImageViewResource(R.id.iv_notify, R.mipmap.app_alarm);
        remoteViews.setTextViewText(R.id.tv_notify_title, title);
        remoteViews.setTextViewText(R.id.tv_notify_content, content);

        setOnClick(remoteViews, AlarmNotification.CLOSE_BUTTON, R.id.tv_notify_close);
        setOnClick(remoteViews, AlarmNotification.REMIND_BUTTON, R.id.tv_notify_remind);
        return remoteViews;
    }

    public void setOnClick(RemoteViews remoteViews, String buttonName, int viewId) {
        Intent buttonIntent = new Intent(this, AlarmNotification.class);
        buttonIntent.putExtra(AlarmNotification.BUTTON_NAME, buttonName);
        PendingIntent intentClose = PendingIntent.getBroadcast(this, 1,
                buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(viewId, intentClose);
    }

    public NotificationCompat.Builder getNotification_25(String title, String content) {
        RemoteViews remoteViews = getRemoteViews(title, content);
        return new NotificationCompat.Builder(getApplicationContext())
                .setAutoCancel(true)
                .setCustomContentView(remoteViews);
    }

    public void sendNotification(String title, String content) {
        if (Build.VERSION.SDK_INT >= 26) {
            createNotificationChannel();
            Notification.Builder builder = getChannelNotification(title, content);
            Notification notification = builder.build();
            getManager().notify(1, notification);
        } else {
            Notification notification = getNotification_25(title, content).build();
            getManager().notify(1, notification);
        }
    }

}
