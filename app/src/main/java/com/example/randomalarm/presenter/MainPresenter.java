package com.example.randomalarm.presenter;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.randomalarm.alarm.AlarmMangerClass;
import com.example.randomalarm.contract.MainContract;
import com.example.randomalarm.model.AlarmSettingModel;
import com.example.randomalarm.model.DefaultSettingModel;
import com.example.randomalarm.setting.AlarmSettingInfo;

import java.util.List;

/**
 * author : Mzz
 * date : 2019 2019/4/27 17:59
 * description :
 */
public class MainPresenter implements MainContract.Presenter {
    private DefaultSettingModel defaultSettingModel;
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
        if (defaultSettingModel == null) {
            defaultSettingModel = new DefaultSettingModel(mainView.getContext());
        }

        AlarmSettingInfo alarmSettingInfo = defaultSettingModel.loadDefaultAlarmSetting();
        //使ID自增
        alarmSettingInfo.setId(new AlarmSettingInfo().getId());
        return alarmSettingInfo;
    }

}
