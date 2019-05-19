package com.example.randomalarm.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.example.randomalarm.common.DateHelper;
import com.example.randomalarm.common.ViewerHelper;
import com.example.randomalarm.model.AlarmSettingModel;
import com.example.randomalarm.notify.NotificationReceiver;
import com.example.randomalarm.setting.AlarmCalendar;
import com.example.randomalarm.setting.AlarmSettingInfo;

import java.util.Calendar;
import java.util.List;

public class AlarmMangerClass {
    private AlarmManager alarmManager;
    private Context context;

    public AlarmMangerClass(Context context) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.context = context;
    }

    private static void setAlarmTime(AlarmManager alarmManager, PendingIntent pi, long timeInMillis) {
        //如果设置的起始时间小于当前时间，闹钟将会马上被触发
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pi);
        } else {
            alarmManager.setWindow(AlarmManager.RTC_WAKEUP, timeInMillis, 100, pi);
        }
    }

    /**
     * 初始化所有闹钟
     */
    public void intiAllAlarms() {
        List <AlarmSettingInfo> sortedAlarmInfos = AlarmSettingModel.getSortedAlarmInfos();
        intiAllAlarms(sortedAlarmInfos);
    }

    /**
     * 初始化所有闹钟
     *
     * @param sortedAlarmInfos
     */
    public void intiAllAlarms(List <AlarmSettingInfo> sortedAlarmInfos) {
        for (AlarmSettingInfo sortedAlarmInfo : sortedAlarmInfos) {
            if (sortedAlarmInfo.getIsOpenStatus()) {
                AlarmCalendar.removeInvalidDate(sortedAlarmInfo.getAlarmCalendars());
                setFirstAlarm(sortedAlarmInfo, false);
            }
        }
    }

    /**
     * 设置闹钟的第一个提醒时间
     *
     * @param alarmSettingInfo
     * @param isShowRemind     是否提示响铃时间
     */
    public void setFirstAlarm(AlarmSettingInfo alarmSettingInfo, boolean isShowRemind) {
        if (isAlarmClosed(alarmSettingInfo)) return;

        int hour = alarmSettingInfo.getHour();
        int minute = alarmSettingInfo.getMinute();
        Calendar nextCalendar = DateHelper.getCalendarByHourAndMinute(hour, minute);
        Calendar nowTime = DateHelper.getNowTime();
        int nowHour = DateHelper.getNowHour();
        int nowMinute = DateHelper.getNowMinute();
        //若设置的闹钟时间小于当前时间，或小时和分钟都等于当前的，则添加1天，避免当天重复触发闹钟
        if (nextCalendar.compareTo(nowTime) < 0 || (hour == nowHour && minute == nowMinute)) {
            nextCalendar.add(Calendar.DATE, 1);
        }
        nextCalendar = alarmSettingInfo.getNextAlarmDate(nextCalendar);
        setAlarm(alarmSettingInfo, nextCalendar, isShowRemind);
    }

    /**
     * 设置下一重复间隔的闹钟
     *
     * @param alarmSettingInfo
     */
    public void setNextIntervalAlarm(AlarmSettingInfo alarmSettingInfo) {
        if (isAlarmClosed(alarmSettingInfo)) return;

        Calendar calendar = alarmSettingInfo.getNextIntervalAlarm();
        if (calendar == null) {
            //当天不存在下一间隔闹钟，则直接设置下一天闹钟
            setFirstAlarm(alarmSettingInfo, false);
        } else {
            setAlarm(alarmSettingInfo, calendar, false);
        }
    }

    /**
     * 闹钟已经关闭
     *
     * @param alarmSettingInfo
     * @return
     */
    private boolean isAlarmClosed(AlarmSettingInfo alarmSettingInfo) {
        if (!alarmSettingInfo.getIsOpenStatus()) {
            cancelAlarm(alarmSettingInfo.getId());
            return true;
        }
        return false;
    }

    private void setAlarm(AlarmSettingInfo alarmSettingInfo, Calendar calendar, boolean isShowRemind) {
        if (calendar == null) {
            return;
        }

        Log.w("nextCalendar", DateHelper.formatDateAndHHmm(calendar));
        long nextTimeInMillis = calendar.getTimeInMillis();
        long nowMillis = System.currentTimeMillis();
        long diffMillis = nextTimeInMillis - nowMillis;
        if (isShowRemind) {
            String diff = DateHelper.getAlarmRemainedTime(diffMillis);
            ViewerHelper.showToast(context, diff);
        }
        PendingIntent pi = getAlarmPendingIntent(alarmSettingInfo.getId());
//        PendingIntent pi = getNotifyPendingIntent(alarmSettingInfo.getId());
        //先取消之前的闹钟
        alarmManager.cancel(pi);
        setAlarmTime(alarmManager, pi, nextTimeInMillis);
    }

    private PendingIntent getAlarmPendingIntent(Long id) {
//        Intent intent = new Intent(context, AlarmRemindActivity.class);
//        //可能在服务中启动activity，所以需要添加这个标记
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra(AlarmConstant.ALARM_ID, id.longValue());
        Intent intent = getIntent(id, AlarmRemindActivity.class);
        return PendingIntent.getActivity(context, id.intValue(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent getNotifyPendingIntent(Long id) {
        Intent intent = getIntent(id, NotificationReceiver.class);
        return PendingIntent.getBroadcast(context, id.intValue(),
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private Intent getIntent(Long id, Class <?> cls) {
        Intent intent = new Intent(context, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(AlarmConstant.ALARM_ID, id.longValue());
        return intent;
    }

    /**
     * 取消闹钟
     *
     * @param id
     */
    public void cancelAlarm(Long id) {
        PendingIntent pi = getAlarmPendingIntent(id);
        alarmManager.cancel(pi);
    }

}
