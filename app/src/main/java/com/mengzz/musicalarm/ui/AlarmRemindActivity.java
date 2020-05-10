package com.mengzz.musicalarm.ui;

import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.mengzz.musicalarm.R;
import com.mengzz.musicalarm.alarm.AlarmConstant;
import com.mengzz.musicalarm.alarm.AlarmMangerClass;
import com.mengzz.musicalarm.model.AlarmSettingModel;
import com.mengzz.musicalarm.setting.AlarmSettingInfo;
import com.mengzz.musicalarm.setting.AppSetting;
import com.mzz.zandroidcommon.common.DateHelper;
import com.mzz.zandroidcommon.common.MediaPlayHelper;
import com.mzz.zandroidcommon.common.VibrateHandler;

import java.io.File;
import java.util.Calendar;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AlarmRemindActivity extends AppCompatActivity {

    private static final int CLOSE_DELAY = 500;
    private static final long[] VIBRATE_PATTERN = {500, 400};
    private VibrateHandler vibrateHandler;
    private MediaPlayHelper mediaPlayHelper;
    private AlarmSettingInfo alarmSettingInfo;
    private ScheduledExecutorService scheduledExecutorService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long alarmId = getIntent().getLongExtra(AlarmConstant.ALARM_ID, -1);
        alarmSettingInfo = AlarmSettingModel.getAlarmSettingInfoById(alarmId);
        if (alarmSettingInfo == null || !verifyAlarmTime()) {
            finish();
            return;
        }
        scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_alarm_remind);
        String ringTitle = playMedia();
        showFragment(ringTitle);
        int duration = alarmSettingInfo.getDuration() * DateHelper.MINUTE_TO_MILLIS;
        finishInTime(duration);
    }

    @Override
    public void onDestroy() {
        if (mediaPlayHelper != null) {
            mediaPlayHelper.release();
        }
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdownNow();
        }
        if (vibrateHandler != null) {
            vibrateHandler.cancel();
        }
        super.onDestroy();
    }

    private boolean verifyAlarmTime() {
        Calendar recentAlarmCalendar = alarmSettingInfo.getRecentAlarmCalendar();
        long diffTime = recentAlarmCalendar.getTimeInMillis() - System.currentTimeMillis();
        //闹钟时间与当前时间相差1分钟之内
        return diffTime < DateHelper.MINUTE_TO_MILLIS && alarmSettingInfo.getMinute() == DateHelper.getNowMinute();
    }

    private void showFragment(String ringTitle) {
        AppSetting setting = AppSetting.readSetting(this);
        RemindFragment remindFragment = RemindFragment.newInstance(ringTitle,
                setting.getShowedImagePath(), alarmSettingInfo.getRemark());
        remindFragment.setAlarmHandler(new RemindFragment.AlarmHandler() {
            @Override
            public void setNextIntervalAlarm() {
                AlarmRemindActivity.this.setNextIntervalAlarm();
            }

            @Override
            public void setFirstAlarm() {
                new AlarmMangerClass(AlarmRemindActivity.this).setFirstAlarm(alarmSettingInfo,
                        false);
                finishInTime(CLOSE_DELAY);
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_remind,
                remindFragment).commit();
    }

    private String playMedia() {
        String playedSongPath = alarmSettingInfo.getPlayedSongPath();
        String ringTitle = "铃声：";
        File file = new File(playedSongPath);
        if (file.exists()) {
            Uri uri = Uri.fromFile(file);
            mediaPlayHelper = new MediaPlayHelper(this, uri);
            ringTitle += file.getName();
        } else {
            mediaPlayHelper = new MediaPlayHelper(this, R.raw.dawn);
            ringTitle += "默认";
        }
        //重复播放
        mediaPlayHelper.setLoopPlay();
        mediaPlayHelper.start();
        vibrateOrNot();
        return ringTitle;
    }

    private void vibrateOrNot() {
        if (alarmSettingInfo.getIsVibrated()) {
            vibrateHandler = new VibrateHandler(this);
            vibrateHandler.vibrate(VIBRATE_PATTERN, 0);
        }
    }

    /**
     * 设置下一个重复间隔的闹钟
     */
    private void setNextIntervalAlarm() {
        new AlarmMangerClass(AlarmRemindActivity.this).setNextIntervalAlarm(alarmSettingInfo);
        finishInTime(CLOSE_DELAY);
    }

    private void thisFinish() {
        this.finish();
    }

    private void finishInTime(int delayMillis) {
        scheduledExecutorService.schedule(this::thisFinish, delayMillis, TimeUnit.MILLISECONDS);
    }

}
