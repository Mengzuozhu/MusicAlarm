package com.example.randomalarm.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.example.randomalarm.R;
import com.example.randomalarm.setting.RemindFragment;
import com.example.randomalarm.common.DateHelper;
import com.example.randomalarm.common.MediaPlayHelper;
import com.example.randomalarm.common.VibrateHandler;
import com.example.randomalarm.model.AlarmSettingModel;
import com.example.randomalarm.setting.AlarmSettingInfo;
import com.example.randomalarm.setting.AppSetting;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class AlarmRemindActivity extends AppCompatActivity {

    private static final int CLOSE_DELAY = 500;
    private static final long[] VIBRATE_PATTERN = {500, 400};
    private VibrateHandler vibrateHandler;
    private MediaPlayHelper mediaPlayHelper;
    private Timer finishTimer;
    private AlarmSettingInfo alarmSettingInfo;
    private BroadcastReceiver screenLockReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //点击锁屏键后，表示稍后提醒
            if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                setNextIntervalAlarm();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_alarm_remind);

        long alarmId = getIntent().getLongExtra(AlarmConstant.ALARM_ID, -1);
        if (alarmId == -1) {
            return;
        }
        alarmSettingInfo = AlarmSettingModel.getAlarmSettingInfoById(alarmId);
        if (alarmSettingInfo == null) {
            return;
        }
        registerReceiver(screenLockReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
        vibrateHandler = new VibrateHandler(this);
        String ringTitle = playMedia();
        showFragment(ringTitle);
    }

    public void showFragment(String ringTitle) {
        AppSetting setting = AppSetting.readSetting(this);
        RemindFragment remindFragment = RemindFragment.newInstance(ringTitle, setting.getRemindImagePath());
        remindFragment.setAlarmOnLister(new RemindFragment.AlarmHandler() {
            @Override
            public void setNextIntervalAlarm() {
                AlarmRemindActivity.this.setNextIntervalAlarm();
            }

            @Override
            public void setFirstAlarm() {
                new AlarmMangerClass(AlarmRemindActivity.this).setFirstAlarm(alarmSettingInfo, false);
                finishInTime(CLOSE_DELAY);
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_remind, remindFragment).commit();
    }

    public String playMedia() {
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
        int duration = alarmSettingInfo.getDuration() * DateHelper.MINUTE_TO_MILLIS;
        finishInTime(duration);
        return ringTitle;
    }

    public void vibrateOrNot() {
        if (alarmSettingInfo.getIsVibrated()) {
            vibrateHandler.vibrate(VIBRATE_PATTERN, 0);
        }
    }

    /**
     * 设置下一个重复间隔的闹钟
     */
    public void setNextIntervalAlarm() {
        new AlarmMangerClass(AlarmRemindActivity.this).setNextIntervalAlarm(alarmSettingInfo);
        finishInTime(CLOSE_DELAY);
    }

    public void thisFinish() {
        this.finish();
    }

    public void finishInTime(int delayMillis) {
        finishTimer = new Timer();
        finishTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                thisFinish();
            }
        }, delayMillis);
    }

    @Override
    public void onDestroy() {
        if (mediaPlayHelper != null) {
            mediaPlayHelper.release();
        }
        if (finishTimer != null) {
            finishTimer.cancel();
            finishTimer = null;
        }
        if (vibrateHandler != null) {
            vibrateHandler.cancel();
        }
        unregisterReceiver(screenLockReceiver);
        super.onDestroy();
    }

}
