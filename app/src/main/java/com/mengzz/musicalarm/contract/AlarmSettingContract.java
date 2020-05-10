package com.mengzz.musicalarm.contract;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.mengzz.musicalarm.setting.AlarmSettingInfo;

/**
 * author : Mzz
 * date : 2019 2019/5/2 19:58
 * description :
 */
public interface AlarmSettingContract {
    interface Model {
    }

    interface View {

        Context getContext();

        TimePicker getTimePicker();

        void save(AlarmSettingInfo alarmSettingInfo);

        void showSongPathsActivity(AlarmSettingInfo alarmSettingInfo);

        void showMultCalendarActivity(AlarmSettingInfo alarmSettingInfo);

        RecyclerView getRecyclerView();
    }

    interface Presenter {
        void initAlarm();

        void showSetting(int position, TextView textView);

        void saveAlarmSetting();

        void showSongPathsSetting();

        void saveSetting(int resultCode, Intent data);
    }
}
