package com.example.randomalarm.contract;

import android.content.Context;

import com.example.randomalarm.base.BasePresenter;
import com.example.randomalarm.base.BaseView;
import com.example.randomalarm.setting.AlarmSettingInfo;

import java.util.List;

/**
 * author : Mzz
 * date : 2019 2019/4/27 17:59
 * description :
 */
public interface MainContract {
    interface Model {
        List <AlarmSettingInfo> getAlarm();

    }

    interface View extends BaseView <Presenter> {
        void initOrRefreshAlarm(List <AlarmSettingInfo> data);
        Context getContext();
    }

    interface Presenter extends BasePresenter {
        void initOrRefreshAlarm();

        void deleteByKey(Iterable <Long> key);

        void insertOrReplace(AlarmSettingInfo alarmSettingInfo);

        AlarmSettingInfo getNewAlarm();

//        void setAlarm(AlarmSettingInfo alarmSettingInfo);
    }
}
