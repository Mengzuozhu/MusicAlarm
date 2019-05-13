package com.example.randomalarm.alarm;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.randomalarm.R;
import com.example.randomalarm.common.DateHelper;
import com.example.randomalarm.common.EventBusHelper;
import com.example.randomalarm.model.AlarmSettingModel;
import com.example.randomalarm.setting.AlarmSettingInfo;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import tyrantgit.explosionfield.ExplosionField;

public class AlarmActivity extends AppCompatActivity {

    public static final String ALARM_ID = "ALARM_ID";
    private static final int CLOSE_DELAY = 500;
    @BindView(R.id.tv_remind)
    TextView tvRemind;
    @BindView(R.id.tv_close)
    TextView tvClose;
    @BindView(R.id.tv_real_time)
    TextView tvRealTime;
    @BindView(R.id.tv_alarm_song)
    TextView tvAlarmSong;
    @BindView(R.id.iv_moon_remind)
    ImageView ivMoonRemind;
    @BindView(R.id.iv_sun_close)
    ImageView ivSunClose;
    TimeTickReceiver timeTickReceiver;
    private ExplosionField mExplosionField;
    private Vibrator vibrator;
    private MediaPlayer mediaPlayer;
    private Timer timer;
    private AlarmSettingInfo alarmSettingInfo;
    private BroadcastReceiver screenLockReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //锁屏后，稍后提醒
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
        ButterKnife.bind(this);
        EventBusHelper.register(this);

        timeTickReceiver = new TimeTickReceiver();
        registerReceiver(timeTickReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
        registerReceiver(screenLockReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
        showRealTime(Calendar.getInstance());

        mExplosionField = ExplosionField.attach2Window(this);
        Intent intent = getIntent();
        long alarmId = intent.getLongExtra(ALARM_ID, -1);
        if (alarmId == -1) {
            return;
        }
        vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        alarmSettingInfo = AlarmSettingModel.getAlarmSettingInfoById(alarmId);
        playMedia();
    }

    @Subscribe
    public void getRealTimeEvent(Calendar calendar) {
        showRealTime(calendar);
    }

    public void showRealTime(Calendar calendar) {
        tvRealTime.setText(DateHelper.format(calendar.getTime()));
    }

    public void playMedia() {
        String playedSongPath = alarmSettingInfo.getPlayedSongPath();
        String title = "铃声：";
        File file = new File(playedSongPath);
        if (file.exists()) {
            Uri uri = Uri.fromFile(file);
            mediaPlayer = MediaPlayer.create(this, uri);
            title += file.getName();
        } else {
            mediaPlayer = MediaPlayer.create(this, R.raw.dawn);
            title += "默认";
        }
        tvAlarmSong.setText(title);
        //重复播放
        mediaPlayer.setOnCompletionListener(mp -> {
            if (mediaPlayer != null) {
                mediaPlayer.start();
            }
        });
        mediaPlayer.start();
        vibrateOrNot();
        int duration = alarmSettingInfo.getDuration() * DateHelper.MINUTE_TO_MILLIS;
        closeInTime(duration);
    }

    public void vibrateOrNot() {
        if (alarmSettingInfo.getIsVibrated() && vibrator.hasVibrator()) {
            vibrator.cancel();
            long[] pattern = {500, 400};
            vibrator.vibrate(pattern, 0);
        }
    }

    /**
     * 设置下一个重复间隔的闹钟
     */
    public void setNextIntervalAlarm() {
        new AlarmMangerClass(AlarmActivity.this).setNextIntervalAlarm(alarmSettingInfo);
        closeInTime(CLOSE_DELAY);
    }

    /**
     * 设置下一天的闹钟
     */
    public void setNextDayFirstAlarm() {
        new AlarmMangerClass(AlarmActivity.this).setNextDayFirstAlarm(alarmSettingInfo, false);
        closeInTime(CLOSE_DELAY);
    }

    public void thisFinish() {
        this.finish();
    }

    public void closeInTime(int delayMillis) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                thisFinish();
            }
        }, delayMillis);
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (vibrator != null) {
            vibrator.cancel();
        }
        EventBusHelper.unregister(this);
        unregisterReceiver(timeTickReceiver);
        unregisterReceiver(screenLockReceiver);
        super.onDestroy();
    }

    public void close_onClick(View view) {
        explode(ivSunClose);
        explode(tvClose);
        setNextDayFirstAlarm();
    }

    public void remind_onClick(View view) {
        explode(ivMoonRemind);
        explode(tvRemind);
        setNextIntervalAlarm();
    }

    public void explode(View view) {
        mExplosionField.explode(view);
        view.setOnClickListener(null);
    }
}
