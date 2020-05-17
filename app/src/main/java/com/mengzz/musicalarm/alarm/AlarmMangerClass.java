package com.mengzz.musicalarm.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.mengzz.musicalarm.setting.AlarmCalendar;
import com.mengzz.musicalarm.setting.AlarmSettingInfo;
import com.mengzz.musicalarm.ui.AlarmRemindActivity;
import com.mzz.zandroidcommon.common.DateHelper;
import com.mzz.zandroidcommon.view.ViewerHelper;

import java.util.Calendar;
import java.util.List;

public class AlarmMangerClass {
    private AlarmManager alarmManager;
    private Context context;

    public AlarmMangerClass(Context context) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.context = context;
    }

    private static void setAlarmTime(AlarmManager alarmManager, PendingIntent pi,
                                     long timeInMillis) {
        //先取消之前的闹钟
        alarmManager.cancel(pi);
        //如果设置的起始时间小于当前时间，闹钟将会马上被触发
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pi);
    }

    /**
     * 初始化所有闹钟
     *
     * @param sortedAlarmInfos the sorted alarm infos
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
     * @param alarmSettingInfo the alarm setting info
     * @param isShowRemind     是否提示响铃时间
     */
    public void setFirstAlarm(AlarmSettingInfo alarmSettingInfo, boolean isShowRemind) {
        if (isAlarmClosed(alarmSettingInfo)) {
            return;
        }

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
     * @param alarmSettingInfo the alarm setting info
     */
    public void setNextIntervalAlarm(AlarmSettingInfo alarmSettingInfo) {
        if (isAlarmClosed(alarmSettingInfo)) {
            return;
        }

        Calendar calendar = alarmSettingInfo.getNextIntervalAlarm();
        if (calendar == null) {
            //当天不存在下一间隔闹钟，则直接设置下一天闹钟
            setFirstAlarm(alarmSettingInfo, false);
        } else {
            setAlarm(alarmSettingInfo, calendar, false);
        }
    }

    private boolean isAlarmClosed(AlarmSettingInfo alarmSettingInfo) {
        if (!alarmSettingInfo.getIsOpenStatus()) {
            cancelAlarm(alarmSettingInfo.getId());
            return true;
        }
        return false;
    }

    private void setAlarm(AlarmSettingInfo alarmSettingInfo, Calendar calendar,
                          boolean isShowRemind) {
        if (calendar == null) {
            return;
        }

        long nextTimeInMillis = calendar.getTimeInMillis();
        if (isShowRemind) {
            long remainedMillis = nextTimeInMillis - System.currentTimeMillis();
            String remainedTime = DateHelper.getAlarmRemainedTime(remainedMillis);
            ViewerHelper.showToast(context, remainedTime);
        }
        PendingIntent pi = getAlarmPendingActivity(alarmSettingInfo.getId());
        setAlarmTime(alarmManager, pi, nextTimeInMillis);
    }

    private PendingIntent getAlarmPendingActivity(Long id) {
        Intent intent = getIntent(id, AlarmRemindActivity.class);
        return PendingIntent.getActivity(context, id.intValue(), intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private Intent getIntent(Long id, Class <?> cls) {
        Intent intent = new Intent(context, cls);
        //可能在服务中启动activity，所以需要添加这个标记
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(AlarmConstant.ALARM_ID, id.longValue());
        return intent;
    }

    /**
     * 取消闹钟
     *
     * @param id the id
     */
    public void cancelAlarm(Long id) {
        PendingIntent pi = getAlarmPendingActivity(id);
        alarmManager.cancel(pi);
    }

}
