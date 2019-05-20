package com.example.randomalarm.contract;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.randomalarm.base.BasePresenter;
import com.example.randomalarm.base.BaseView;
import com.example.randomalarm.setting.AlarmCalendar;
import com.example.randomalarm.setting.AlarmSettingInfo;

import java.util.ArrayList;

/**
 * author : Mzz
 * date : 2019 2019/5/2 19:58
 * description :
 */
public interface AlarmSettingContract {
    interface Model {
    }

    interface View extends BaseView <AlarmSettingContract.Presenter> {

        Context getContext();

        TimePicker getTimePicker();

        void save(AlarmSettingInfo alarmSettingInfo);

        void showSongPathsActivity(AlarmSettingInfo alarmSettingInfo);

        void showMultCalendarActivity(AlarmSettingInfo alarmSettingInfo);

        RecyclerView getRecyclerView();
    }

    interface Presenter extends BasePresenter {
        void initAlarm();

        void showSetting(int position, TextView textView);

        void saveAlarmSetting();

        void showSongPathsSetting();

        void setAlarmSettingInfo(AlarmSettingInfo alarmSettingInfo);

        void setAlarmCalendar(ArrayList <AlarmCalendar> alarmCalendars);
    }
}
