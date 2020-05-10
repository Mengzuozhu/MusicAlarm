package com.mengzz.musicalarm.alarm;

import android.app.AlertDialog;
import android.content.Context;

import com.mengzz.musicalarm.R;
import com.mengzz.musicalarm.setting.AlarmCalendar;
import com.mengzz.musicalarm.setting.AlarmSettingInfo;
import com.mzz.zandroidcommon.common.MediaPlayHelper;

import java.util.ArrayList;

/**
 * author : Mzz
 * date : 2019 2019/5/12 21:45
 * description :
 */
public class SurprisedAlarm {

    private static final int HOUR = 8;
    private static final int MINUTE = 1;
    private AlertDialog alertDialog;
    private Context context;
    private MediaPlayHelper mediaPlayHelper;

    public SurprisedAlarm(Context context) {
        this.context = context;
    }

    /**
     * Show surprise dialog.
     *
     * @param alarmSettingInfo the alarm setting info
     */
    public void showSurpriseDialog(AlarmSettingInfo alarmSettingInfo) {
        if (alarmSettingInfo.getHour() != HOUR || alarmSettingInfo.getMinute() != MINUTE) {
            return;
        }
        ArrayList <AlarmCalendar> alarmCalendars = alarmSettingInfo.getAlarmCalendars();
        boolean isNotSelectDay = true;
        for (AlarmCalendar alarmCalendar : alarmCalendars) {
            if (alarmCalendar.getMonth() == 6 && alarmCalendar.getDay() == 18) {
                isNotSelectDay = false;
                break;
            }
        }
        //不是选中日期
        if (isNotSelectDay) {
            return;
        }
        mediaPlayHelper = new MediaPlayHelper(context, R.raw.there);
        //播放完成后，自动关闭
        mediaPlayHelper.setOnCompletionListener(mp -> close());
        mediaPlayHelper.start();
        alertDialog.show();
    }

    /**
     * Close.
     */
    public void close() {
        if (mediaPlayHelper != null) {
            mediaPlayHelper.release();
        }
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }
}
