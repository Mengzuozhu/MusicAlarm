package com.mengzz.musicalarm.contract;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.mengzz.musicalarm.setting.AlarmSettingInfo;

/**
 * author : Mzz
 * date : 2019 2019/4/27 17:59
 * description :
 */
public interface MainContract {

    interface View {

        RecyclerView getRecyclerView();

        FragmentActivity getActivity();
    }

    interface Presenter {
        void initOrRefreshAlarm();

        void deleteByKey(Iterable <Long> key);

        void insertOrReplace(AlarmSettingInfo alarmSettingInfo);

        void showAlarmSettingActivity(AlarmSettingInfo alarmSettingInfo);

        void showEditActivity();

        AlarmSettingInfo getNewAlarm();

    }
}
