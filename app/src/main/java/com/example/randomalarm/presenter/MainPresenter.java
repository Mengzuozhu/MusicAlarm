package com.example.randomalarm.presenter;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.randomalarm.alarm.AlarmMangerClass;
import com.example.randomalarm.common.DateHelper;
import com.example.randomalarm.contract.MainContract;
import com.example.randomalarm.model.AlarmSettingModel;
import com.example.randomalarm.setting.AlarmRepeatMode;
import com.example.randomalarm.setting.AlarmSettingInfo;
import com.example.randomalarm.song.SongInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * author : Mzz
 * date : 2019 2019/4/27 17:59
 * description :
 */
public class MainPresenter implements MainContract.Presenter {
    private MainContract.View mainView;
    private AlarmMangerClass alarmHandler;

    public MainPresenter(MainContract.View mainView) {
        this.mainView = mainView;
        alarmHandler = new AlarmMangerClass(mainView.getContext());
        mainView.setPresenter(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void initOrRefreshAlarm() {
        List <AlarmSettingInfo> sortedAlarmInfos = AlarmSettingModel.getSortedAlarmInfos();
        mainView.initOrRefreshAlarm(sortedAlarmInfos);
        alarmHandler.intiAllAlarms(sortedAlarmInfos);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void deleteByKey(Iterable <Long> keys) {
        for (Long key : keys) {
            alarmHandler.cancelAlarm(key);
        }
        AlarmSettingModel.deleteByKey(keys);
        initOrRefreshAlarm();
    }

    @Override
    public void insertOrReplace(AlarmSettingInfo alarmSettingInfo) {
        AlarmSettingModel.insertOrReplace(alarmSettingInfo);
        alarmHandler.setNextDayFirstAlarm(alarmSettingInfo, true);
    }

    @Override
    public AlarmSettingInfo getNewAlarm() {
        AlarmSettingInfo alarmSettingInfo = new AlarmSettingInfo();
        alarmSettingInfo.setHour(DateHelper.getNowHour());
        alarmSettingInfo.setMinute(DateHelper.getNowMinute());
        alarmSettingInfo.setAlarmRepeatMode(AlarmRepeatMode.toArrayList());
        ArrayList <SongInfo> songPaths = new ArrayList <>();
        alarmSettingInfo.setSongInfos(songPaths);
        return alarmSettingInfo;
    }

}
