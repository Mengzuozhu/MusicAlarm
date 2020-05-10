package com.mengzz.musicalarm.presenter;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mengzz.musicalarm.R;
import com.mengzz.musicalarm.adapter.MainAdapter;
import com.mengzz.musicalarm.alarm.AlarmMangerClass;
import com.mengzz.musicalarm.contract.MainContract;
import com.mengzz.musicalarm.edit.EditItemInfo;
import com.mengzz.musicalarm.model.AlarmSettingModel;
import com.mengzz.musicalarm.model.DefaultSettingModel;
import com.mengzz.musicalarm.setting.AlarmSettingInfo;
import com.mengzz.musicalarm.ui.AlarmSettingActivity;
import com.mengzz.musicalarm.ui.LinearEditActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * author : Mzz
 * date : 2019 2019/4/27 17:59
 * description :
 */
public class MainPresenter implements MainContract.Presenter {
    private MainAdapter mainAdapter;
    private RecyclerView rvAlarm;
    private List <AlarmSettingInfo> alarmSettingInfos;
    private FragmentActivity activity;
    private DefaultSettingModel defaultSettingModel;
    private MainContract.View mainView;
    private AlarmMangerClass alarmHandler;

    public MainPresenter(MainContract.View mainView) {
        this.mainView = mainView;
        activity = mainView.getActivity();
        alarmHandler = new AlarmMangerClass(activity);
        rvAlarm = mainView.getRecyclerView();
    }

    @Override
    public void initOrRefreshAlarm() {
        alarmSettingInfos = AlarmSettingModel.getSortedAlarmInfos();
        initOrRefreshAlarm(alarmSettingInfos);
        alarmHandler.intiAllAlarms(alarmSettingInfos);
    }

    private void initOrRefreshAlarm(List <AlarmSettingInfo> alarmSettingInfos) {
        this.alarmSettingInfos = alarmSettingInfos;
        if (mainAdapter != null) {
            mainAdapter.setNewData(alarmSettingInfos);
        } else {
            initAlarm();
        }
    }

    private void initAlarm() {
        mainAdapter = new MainAdapter(R.layout.item_alarm, alarmSettingInfos);
        setOnItemClickListener();
        mainAdapter.setCheckAlarmListener(this);
        rvAlarm.setLayoutManager(new LinearLayoutManager(activity));
        rvAlarm.setAdapter(mainAdapter);
    }

    private void setOnItemClickListener() {
        mainAdapter.setOnItemClickListener((adapter, view, position) -> {
            AlarmSettingInfo alarm = (AlarmSettingInfo) adapter.getItem(position);
            if (alarm != null) {
                showAlarmSettingActivity(alarm);
            }
        });
        mainAdapter.setOnItemLongClickListener((adapter, view, position) -> {
            showEditActivity();
            return false;
        });
    }

    @Override
    public void showAlarmSettingActivity(AlarmSettingInfo alarmSettingInfo) {
        AlarmSettingActivity.startForResult(activity, alarmSettingInfo);
    }

    @Override
    public void showEditActivity() {
        ArrayList <EditItemInfo> editData = new ArrayList <>();
        for (AlarmSettingInfo settingInfo : alarmSettingInfos) {
            editData.add(new EditItemInfo(settingInfo.getId().intValue(),
                    settingInfo.getShowedTime()));
        }
        LinearEditActivity.startForResult(activity, editData);
    }

    @Override
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
        alarmHandler.setFirstAlarm(alarmSettingInfo, true);
    }

    @Override
    public AlarmSettingInfo getNewAlarm() {
        if (defaultSettingModel == null) {
            defaultSettingModel = new DefaultSettingModel(mainView.getActivity());
        }

        AlarmSettingInfo alarmSettingInfo = defaultSettingModel.loadDefaultAlarmSetting();
        //使ID自增
        alarmSettingInfo.setId(new AlarmSettingInfo().getId());
        return alarmSettingInfo;
    }

}
