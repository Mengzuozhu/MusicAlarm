package com.mengzz.musicalarm.presenter;

import android.content.Intent;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.mengzz.musicalarm.R;
import com.mengzz.musicalarm.adapter.MultipleItem;
import com.mengzz.musicalarm.adapter.MultipleItemQuickAdapter;
import com.mengzz.musicalarm.contract.AlarmSettingContract;
import com.mengzz.musicalarm.multicalendar.MultCalendarActivity;
import com.mengzz.musicalarm.setting.AlarmCalendar;
import com.mengzz.musicalarm.setting.AlarmRepeatMode;
import com.mengzz.musicalarm.setting.AlarmSettingInfo;
import com.mengzz.musicalarm.song.SongInfo;
import com.mengzz.musicalarm.ui.AlarmSongActivity;
import com.mzz.zandroidcommon.common.StringHelper;

import java.util.ArrayList;
import java.util.Collections;
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
    private static final String VIBRATED = "振动";
    private static final String CALENDAR = "日期";
    private static final String REMARK_NAME = "备注";
    private static final String CANCEL_NAME = "取消";
    private static final String CONFIRM = "确定";
    private ArrayList <AlarmCalendar> alarmCalendars;
    private MultipleItemQuickAdapter multipleItemAdapter;
    private AlarmSettingContract.View view;
    private AlarmSettingInfo alarmSettingInfo;
    private ArrayList <SongInfo> songPaths;
    private ArrayList <AlarmRepeatMode> alarmRepeatModes;
    private int repeatFrequency;
    private int interval;
    private int duration;
    private String remark;

    public AlarmSettingPresenter(AlarmSettingContract.View view,
                                 AlarmSettingInfo alarmSettingInfo) {
        this.view = view;
        this.alarmSettingInfo = alarmSettingInfo;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void initAlarm() {
        List <MultipleItem> settings = getSettings();
        initAdapter(settings);
    }

    private List <MultipleItem> getSettings() {
        List <MultipleItem> settings = new ArrayList <>();
        TimePicker timePicker = view.getTimePicker();
        if (timePicker != null) {
            timePicker.setHour(alarmSettingInfo.getHour());
            timePicker.setMinute(alarmSettingInfo.getMinute());
        }
        songPaths = alarmSettingInfo.getSongInfos();
        alarmRepeatModes = alarmSettingInfo.getAlarmRepeatMode();
        interval = alarmSettingInfo.getInterval();
        duration = alarmSettingInfo.getDuration();
        repeatFrequency = alarmSettingInfo.getRepeatFrequency();
        remark = alarmSettingInfo.getRemark();
        alarmCalendars = alarmSettingInfo.getAlarmCalendars();
        alarmCalendars = AlarmCalendar.removeInvalidDate(alarmCalendars);
        Collections.sort(alarmCalendars);
        settings.add(new MultipleItem(MultipleItem.RIGHT_BUTTON,
                getRepeatModeInfo(alarmRepeatModes)));
        settings.add(new MultipleItem(MultipleItem.RIGHT_BUTTON, getAlarmCalendarsInfo()));
        settings.add(new MultipleItem(MultipleItem.RIGHT_BUTTON, getSongPlayModeInfo()));
        settings.add(new MultipleItem(MultipleItem.RIGHT_BUTTON, getIntervalInfo(interval)));
        settings.add(new MultipleItem(MultipleItem.RIGHT_BUTTON, getDurationInfo(duration)));
        settings.add(new MultipleItem(MultipleItem.RIGHT_BUTTON,
                getRepeatFrequencyInfo(repeatFrequency)));
        settings.add(new MultipleItem(MultipleItem.RIGHT_BUTTON, getRemarkInfo(remark)));
        settings.add(new MultipleItem(MultipleItem.SWITCH, VIBRATED,
                alarmSettingInfo.getIsVibrated()));
        return settings;
    }

    private void initAdapter(List <MultipleItem> settings) {
        multipleItemAdapter = new MultipleItemQuickAdapter(settings);
        multipleItemAdapter.setOnItemClickListener((adapter1, view1, position) -> {
            TextView textView = view1.findViewById(R.id.tv_setting_name);
            showSetting(position, textView);
        });
        RecyclerView recyclerView = view.getRecyclerView();
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(multipleItemAdapter);
    }

    private String getRepeatModeInfo(ArrayList <AlarmRepeatMode> repeat) {
        if (repeat == null || repeat.isEmpty()) {
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
        return String.format("%s：%s", RING_NAME, alarmSettingInfo.getPlayedMode().getDesc());
    }

    private String getIntervalInfo(int interval) {
        return StringHelper.getLocalFormat("%s：%d 分钟", INTERVAL_NAME, interval);
    }

    private String getDurationInfo(int duration) {
        return StringHelper.getLocalFormat("%s：%d 分钟", DURATION_NAME, duration);
    }

    private String getRepeatFrequencyInfo(int repeatNumber) {
        return StringHelper.getLocalFormat("%s：%d", REPEAT_FREQUENCY_NAME, repeatNumber);
    }

    private String getAlarmCalendarsInfo() {
        StringBuilder info = new StringBuilder(CALENDAR);
        info.append("：");
        int size = alarmCalendars.size();
        for (int i = 0; i < size && i < 3; i++) {
            AlarmCalendar alarmCalendar = alarmCalendars.get(i);
            if (alarmCalendar.isCurYear()) {
                info.append(alarmCalendar.toMonthAndDayString());
            } else {
                info.append(alarmCalendar.toString());
            }
            info.append(" ");
        }

        if (size > 3) {
            info.append("...");
        } else if (size == 0) {
            info.append("无");
        }
        return info.toString();
    }

    private String getRemarkInfo(String remark) {
        if (remark == null) {
            remark = "";
        }
        return StringHelper.getLocalFormat("%s：%s", REMARK_NAME, remark);
    }

    private String[] getIntervals() {
        int len = 6;
        String[] intervals = new String[len];
        for (int i = 0; i < intervals.length; i++) {
            intervals[i] = StringHelper.getLocalFormat("%d 分钟", getInterval(i));
        }
        return intervals;
    }

    private int getInterval(int i) {
        return (i + 1) * 5;
    }

    @Override
    public void showSetting(int position, TextView textView) {
        switch (position) {
            case 0:
                showRepeatModeDialog(textView);
                break;
            case 1:
                view.showMultCalendarActivity(alarmSettingInfo);
                break;
            case 2:
                showSongPathsSetting();
                break;
            case 3:
                showIntervalDialog(textView);
                break;
            case 4:
                showDurationDialog(textView);
                break;
            case 5:
                showRepeatFrequencyDialog(textView);
                break;
            case 6:
                showRemarkDialog(textView);
                break;
        }
    }

    private void showRepeatModeDialog(TextView textView) {
        String[] repeatDesc = AlarmRepeatMode.toDescArray();
        AlarmRepeatMode[] repeatModes = AlarmRepeatMode.values();
        boolean[] checkedItems = new boolean[repeatModes.length];
        for (AlarmRepeatMode alarmRepeatMode : alarmRepeatModes) {
            checkedItems[alarmRepeatMode.getId()] = true;
        }
        getAlertDialog(REPEAT_MODE_NAME).setMultiChoiceItems(repeatDesc, checkedItems,
                (dialog, which, isChecked) -> checkedItems[which] = isChecked)
                .setPositiveButton("确定", (dialog, which) -> {
                    setAlarmRepeatModes(repeatModes, checkedItems);
                    textView.setText(getRepeatModeInfo(alarmRepeatModes));
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
        return new AlertDialog.Builder(view.getContext())
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

    private void showRemarkDialog(TextView textView) {
        EditText et = new EditText(view.getContext());
        et.setText(remark);
        AlertDialog alertDialog = getAlertDialog(REMARK_NAME)
                .setView(et).setPositiveButton(CONFIRM, (dialog, which) -> {
                    remark = et.getText().toString();
                    textView.setText(getRemarkInfo(remark));
                    dialog.dismiss();
                })
                .create();
        alertDialog.show();
    }

    private NumberPicker getNumberPicker(int min, int max, int value) {
        NumberPicker numberPicker = new NumberPicker(view.getContext());
        numberPicker.setMinValue(min);
        numberPicker.setMaxValue(max);
        numberPicker.setValue(value);
        return numberPicker;
    }

    @Override
    public void saveAlarmSetting() {
        updateSetting();
        view.save(alarmSettingInfo);
    }

    private void updateSetting() {
        TimePicker timePicker = view.getTimePicker();
        if (timePicker != null) {
            alarmSettingInfo.setHour(timePicker.getHour());
            alarmSettingInfo.setMinute(timePicker.getMinute());
        }
        alarmSettingInfo.setAlarmRepeatMode(alarmRepeatModes);
        alarmSettingInfo.setSongInfos(songPaths);
        alarmSettingInfo.setInterval(interval);
        alarmSettingInfo.setDuration(duration);
        alarmSettingInfo.setRepeatFrequency(repeatFrequency);
        alarmSettingInfo.setRemark(remark);
        alarmSettingInfo.setIsOpenStatus(true);
        setIsVibrated();
    }

    private void setIsVibrated() {
        int position = multipleItemAdapter.getData().size() - 1;
        Switch aSwitch = (Switch) multipleItemAdapter.getViewByPosition(view.getRecyclerView(),
                position, R.id.swh_status);
        if (aSwitch != null) {
            alarmSettingInfo.setIsVibrated(aSwitch.isChecked());
        }
    }

    @Override
    public void showSongPathsSetting() {
        view.showSongPathsActivity(alarmSettingInfo);
    }

    @Override
    public void saveSetting(int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        if (resultCode == AlarmSongActivity.ALARM_SONG_SET_CODE) {
            this.alarmSettingInfo = data.getParcelableExtra(AlarmSongActivity.ALARM_SONG_INFO);
        } else if (resultCode == MultCalendarActivity.MULT_CALENDAR_CODE) {
            ArrayList <AlarmCalendar> selectAlarmCalendar =
                    data.getParcelableArrayListExtra(MultCalendarActivity.ALARM_CALENDAR_DATA);
            this.alarmSettingInfo.setAlarmCalendars(selectAlarmCalendar);
        }
        //updateSetting();
        initAlarm();
    }

}
