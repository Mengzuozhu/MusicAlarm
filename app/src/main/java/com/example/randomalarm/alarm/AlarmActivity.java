package com.example.randomalarm.alarm;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.example.randomalarm.R;
import com.example.randomalarm.common.DateHelper;
import com.example.randomalarm.model.AlarmSettingModel;
import com.example.randomalarm.setting.AlarmSettingInfo;

import java.io.File;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class AlarmActivity extends AppCompatActivity {

    public static final String ALARM_ID = "ALARM_ID";
    private Vibrator vibrator;
    private MediaPlayer mediaPlayer;
    private Timer timer;
    private AlarmSettingInfo alarmSettingInfo;
    private AlertDialog alertDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_alarm);
        Intent intent = getIntent();
        long alarmId = intent.getLongExtra(ALARM_ID, -1);
        if (alarmId == -1) {
            return;
        }
        vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        alarmSettingInfo = AlarmSettingModel.getAlarmSettingInfoById(alarmId);
        playMedia();
    }

    public void playMedia() {
        String playedSongPath = alarmSettingInfo.getPlayedSongPath();
        String title = "闹钟-歌曲：";
        File file = new File(playedSongPath);
        if (file.exists()) {
            Uri uri = Uri.fromFile(file);
            mediaPlayer = MediaPlayer.create(this, uri);
            title += file.getName();
        } else {
            mediaPlayer = MediaPlayer.create(this, R.raw.dawn);
            title += "默认";
        }

        //重复播放
        mediaPlayer.setOnCompletionListener(mp -> {
            if (mediaPlayer != null) {
                mediaPlayer.start();
            }
        });
        mediaPlayer.start();
        vibrateOrNot();
        showDialog(title);
    }

    public void vibrateOrNot() {
        if (alarmSettingInfo.getIsVibrated() && vibrator.hasVibrator()) {
            vibrator.cancel();
            vibrator.vibrate(new long[]{200, 100}, 0);
        }
    }

    public void showDialog(String title) {
        //创建一个闹钟提醒的对话框,点击确定关闭铃声与页面
        String message = DateHelper.format(new Date());
        String text = "关闭闹铃";
        AlarmActivity context = AlarmActivity.this;
        AlarmMangerClass alarmHandler = new AlarmMangerClass(context);
        alertDialog = new AlertDialog.Builder(context).setTitle(title).setMessage(message)
                .setPositiveButton(text, (dialog, which) -> {
                    //设置下一天的闹钟
                    alarmHandler.setNextDayFirstAlarm(alarmSettingInfo, false);
                    this.finish();
                }).setNegativeButton("稍后提醒", (dialog, which) -> {
                    alarmHandler.setNextIntervalAlarm(alarmSettingInfo);
                    this.finish();
                }).create();
        alertDialog.show();
        closeInTime(alarmSettingInfo.getDuration());
    }

    public void thisFinish() {
        this.finish();
    }

    public void closeInTime(int delayMinute) {
        //分钟转为毫秒
        delayMinute = delayMinute * DateHelper.MINUTE_TO_MILLIS;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                thisFinish();
            }
        }, delayMinute);
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
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        if (vibrator != null) {
            vibrator.cancel();
        }
        super.onDestroy();
    }
}
