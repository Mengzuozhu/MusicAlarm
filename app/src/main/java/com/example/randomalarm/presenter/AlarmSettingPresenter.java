package com.example.randomalarm.presenter;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.randomalarm.common.StringHelper;
import com.example.randomalarm.contract.AlarmSettingContract;
import com.example.randomalarm.setting.AlarmRepeatMode;
import com.example.randomalarm.setting.AlarmSettingInfo;
import com.example.randomalarm.song.SongInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * author : Mzz
 * date : 2019 2019/5/2 19:58
 * description :
 */
public class AlarmSettingPresenter implements AlarmSettingContract.Presenter {

    private static final String REPEAT_MODE_NAME = "重复";
    private static final String RING_NAME = "铃声";
    private static final String INTERVAL_NAME = "再响间隔";
    private static final String DURATION_NAME = "响铃时长";
    private static final String REPEAT_FREQUENCY_NAME = "重复响铃次数";
    private static final String CANCEL_NAME = "取消";
    private static final String CONFIRM = "确定";

    private AlarmSettingContract.View mView;
    private AlarmSettingInfo alarmSettingInfo;
    private ArrayList <SongInfo> songPaths;
    private ArrayList <AlarmRepeatMode> alarmRepeatModes;
    private int repeatFrequency;
    private int interval;
    private int duration;

    public AlarmSettingPresenter(AlarmSettingContract.View mView, AlarmSettingInfo alarmSettingInfo) {
        this.mView = mView;
        this.alarmSettingInfo = alarmSettingInfo;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void initAlarm() {
        List <String> settings = new ArrayList <>();
        TimePicker timePicker = mView.getTimePicker();
        timePicker.setHour(alarmSettingInfo.getHour());
        timePicker.setMinute(alarmSettingInfo.getMinute());
        songPaths = alarmSettingInfo.getSongInfos();
        alarmRepeatModes = alarmSettingInfo.getAlarmRepeatMode();
        interval = alarmSettingInfo.getInterval();
        duration = alarmSettingInfo.getDuration();
        repeatFrequency = alarmSettingInfo.getRepeatFrequency();
        settings.add(getRepeatInfo(alarmRepeatModes));
        settings.add(getSongPlayModeInfo());
        settings.add(getIntervalInfo(interval));
        settings.add(getDurationInfo(duration));
        settings.add(getRepeatFrequencyInfo(repeatFrequency));
        mView.showAlarm(settings);
    }

    private String getRepeatInfo(ArrayList <AlarmRepeatMode> repeat) {
        if (repeat == null || repeat.size() == 0) {
            return REPEAT_MODE_NAME + "：无";
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(REPEAT_MODE_NAME + "：周");
        int size = repeat.size();
        for (int i = 0; i < size; i++) {
            AlarmRepeatMode repeatMode = repeat.get(i);
            stringBuilder.append(repeatMode.getDesc().replace("周", ""));
            if (i < size - 1) {
                stringBuilder.append("、");
            }
        }
        return stringBuilder.toString();
    }

    private String getSongPlayModeInfo() {
        return String.format("%s：%s", RING_NAME, alarmSettingInfo.getSongPlayedMode().getDesc());
    }

    private String getIntervalInfo(int interval) {
        return StringHelper.getLocalFormat("%s：%d 分钟", INTERVAL_NAME, interval);
    }

    private String getDurationInfo(int duration) {
        return String.format("%s：%d 分钟", DURATION_NAME, duration);
    }

    private String getRepeatFrequencyInfo(int repeatNumber) {
        return String.format("%s：%d", REPEAT_FREQUENCY_NAME, repeatNumber);
    }

    private String[] getIntervals() {
        int len = 6;
        String[] intervals = new String[len];
        for (int i = 0; i < intervals.length; i++) {
            intervals[i] = String.format("%d 分钟", getInterval(i));
        }
        return intervals;
    }

    private int getInterval(int i) {
        return (i + 1) * 5;
    }

    private void showRepeatModeDialog(TextView textView) {
        String[] repeatDesc = AlarmRepeatMode.toDescArray();
        AlarmRepeatMode[] repeatModes = AlarmRepeatMode.values();
        boolean[] checkedItems = new boolean[repeatModes.length];
        for (AlarmRepeatMode alarmRepeatMode : alarmRepeatModes) {
            checkedItems[alarmRepeatMode.getId()] = true;
        }
        getAlertDialog(REPEAT_MODE_NAME).setMultiChoiceItems(repeatDesc, checkedItems,
                (dialog, which, isChecked) -> {
                    checkedItems[which] = isChecked;
                })
                .setPositiveButton("确定", (dialog, which) -> {
                    setAlarmRepeatModes(repeatModes, checkedItems);
                    textView.setText(getRepeatInfo(alarmRepeatModes));
                }).create().show();
    }

    private void setAlarmRepeatModes(AlarmRepeatMode[] repeatModes, boolean[] booleans) {
        alarmRepeatModes.clear();
        for (int i = 0; i < booleans.length; i++) {
            if (booleans[i]) {
                alarmRepeatModes.add(repeatModes[i]);
            }
        }
    }

    private void showIntervalDialog(TextView textView) {
        int checkedItem = (interval / 5) - 1;
        String[] intervals = getIntervals();
        getAlertDialog(INTERVAL_NAME)
                .setSingleChoiceItems(intervals, checkedItem, (dialog1, which) -> {
                    interval = getInterval(which);
                    textView.setText(getIntervalInfo(interval));
                    dialog1.dismiss();
                }).create().show();
    }

    private AlertDialog.Builder getAlertDialog(String title) {
        return new AlertDialog.Builder(mView.getContext())
                .setTitle(title)
                .setNegativeButton(CANCEL_NAME, (dialog, which) -> dialog.dismiss());
    }

    private void showRepeatFrequencyDialog(TextView textView) {
        NumberPicker numberPicker = getNumberPicker(1, 10, repeatFrequency);
        AlertDialog alertDialog = getAlertDialog(REPEAT_FREQUENCY_NAME)
                .setView(numberPicker).setPositiveButton(CONFIRM, (dialog, which) -> {
                    repeatFrequency = numberPicker.getValue();
                    textView.setText(getRepeatFrequencyInfo(repeatFrequency));
                    dialog.dismiss();
                })
                .create();
        alertDialog.show();
    }

    private void showDurationDialog(TextView textView) {
        NumberPicker numberPicker = getNumberPicker(1, 30, duration);
        AlertDialog alertDialog = getAlertDialog(DURATION_NAME)
                .setView(numberPicker).setPositiveButton(CONFIRM, (dialog, which) -> {
                    duration = numberPicker.getValue();
                    textView.setText(getDurationInfo(duration));
                    dialog.dismiss();
                })
                .create();
        alertDialog.show();
    }

    private NumberPicker getNumberPicker(int min, int max, int value) {
        NumberPicker numberPicker = new NumberPicker(mView.getContext());
        numberPicker.setMinValue(min);
        numberPicker.setMaxValue(max);
        numberPicker.setValue(value);
        return numberPicker;
    }

    @Override
    public void showSetting(int position, TextView textView) {
        switch (position) {
            case 0:
                showRepeatModeDialog(textView);
                break;
            case 1:
                showSongPathsSetting();
                break;
            case 2:
                showIntervalDialog(textView);
                break;
            case 3:
                showDurationDialog(textView);
                break;
            case 4:
                showRepeatFrequencyDialog(textView);
                break;
        }
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void saveAlarmSetting() {
        TimePicker timePicker = mView.getTimePicker();
        alarmSettingInfo.setHour(timePicker.getHour());
        alarmSettingInfo.setMinute(timePicker.getMinute());
        alarmSettingInfo.setAlarmRepeatMode(alarmRepeatModes);
        alarmSettingInfo.setSongInfos(songPaths);
        alarmSettingInfo.setInterval(interval);
        alarmSettingInfo.setDuration(duration);
        alarmSettingInfo.setRepeatFrequency(repeatFrequency);
        alarmSettingInfo.setIsOpenStatus(true);
        mView.save(alarmSettingInfo);
    }

    @Override
    public void showSongPathsSetting() {
        mView.showSongPathsSetting(alarmSettingInfo);
    }

    @Override
    public void setAlarmSettingInfo(AlarmSettingInfo alarmSettingInfo) {
        this.alarmSettingInfo = alarmSettingInfo;
        initAlarm();
    }

}
