package com.example.randomalarm.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.randomalarm.common.DateHelper;
import com.example.randomalarm.common.ViewerHelper;
import com.example.randomalarm.setting.AlarmSettingInfo;

import java.util.Calendar;

public class AlarmHandlerClass {
    private AlarmManager alarmManager;
    private Context context;

    public AlarmHandlerClass(Context context) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.context = context;
    }

    /**
     * 设置下一天的闹钟
     *
     * @param alarmSettingInfo
     * @param isShowRemind     是否提示响铃时间
     */
    public void setNextDayFirstAlarm(AlarmSettingInfo alarmSettingInfo, boolean isShowRemind) {
        if (isAlarmClosed(alarmSettingInfo)) return;

        int hour = alarmSettingInfo.getHour();
        int minute = alarmSettingInfo.getMinute();
        Calendar nextCalendar = DateHelper.getCalendarByHourAndMinute(hour, minute);
        Calendar nowTime = DateHelper.getNowTime();
        int nowHour = DateHelper.getNowHour();
        int nowMinute = DateHelper.getNowMinute();
        //若设置的闹钟时间小于当前时间，或小时和分钟等于当前的，则添加1天，避免当天重复触发闹钟
        if (nextCalendar.compareTo(nowTime) < 0 || (hour == nowHour && minute == nowMinute)) {
            nextCalendar.add(Calendar.DATE, 1);
        }
        setAlarm(alarmSettingInfo, nextCalendar, isShowRemind);
    }

    /**
     * 设置下一间隔的闹钟
     *
     * @param alarmSettingInfo
     */
    public void setNextIntervalAlarm(AlarmSettingInfo alarmSettingInfo) {
        if (isAlarmClosed(alarmSettingInfo)) return;

        Calendar calendar = alarmSettingInfo.getNextIntervalAlarm();
        if (calendar == null) {
            //直接设置下一天闹钟
            setNextDayFirstAlarm(alarmSettingInfo, false);
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
//        calendar.add(Calendar.SECOND, -50);
        long nextTimeInMillis = calendar.getTimeInMillis();
        long nowMillis = System.currentTimeMillis();
        long diffMillis = nextTimeInMillis - nowMillis;
        if (isShowRemind) {
            String diff = String.format("%s后响铃", DateHelper.getDiffTime(diffMillis));
            ViewerHelper.showToast(context, diff);
        }
        PendingIntent pi = getPendingIntent(alarmSettingInfo.getId());
        //先取消之前的闹钟
        alarmManager.cancel(pi);
        setAlarmTime(alarmManager, pi, nextTimeInMillis);
    }

    private PendingIntent getPendingIntent(Long id) {
        Intent intent = new Intent(context, AlarmActivity.class);
        intent.putExtra(AlarmActivity.ALARM_ID, id.longValue());
        return PendingIntent.getActivity(context, id.intValue(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static void setAlarmTime(AlarmManager alarmManager, PendingIntent pi, long timeInMillis) {
        //如果设置的起始时间小于当前时间，闹钟将会马上被触发
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pi);
        } else {
            alarmManager.setWindow(AlarmManager.RTC_WAKEUP, timeInMillis, 100, pi);
        }
    }

    public void cancelAlarm(Long id) {
        PendingIntent pi = getPendingIntent(id);
        alarmManager.cancel(pi);
    }

//    static void judgeAlarm(Context context) {
//        if (alarmSettingInfos == null) {
//            alarmSettingInfos = AlarmSettingModel.getSortedAlarmInfos();
//        }
//        Calendar calendar = Calendar.getInstance();
//        String triggerTime = DateHelper.getHourAndMinuteString(calendar);
//
//        for (AlarmSettingInfo alarmSettingInfo : alarmSettingInfos) {
//            if (alarmSettingInfo.isTrigger(triggerTime)) {
//                showAlarmActivity(context, alarmSettingInfo);
//                break;
//            }
//        }
//    }

    private static void showAlarmActivity(Context context, AlarmSettingInfo alarmSettingInfo) {
        Intent intent = new Intent(context, AlarmActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(AlarmActivity.ALARM_ID, alarmSettingInfo);
        context.startActivity(intent);
    }

}
