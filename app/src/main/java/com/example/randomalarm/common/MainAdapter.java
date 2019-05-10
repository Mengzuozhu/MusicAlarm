package com.example.randomalarm.common;

import android.support.annotation.Nullable;
import android.widget.Switch;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.randomalarm.R;
import com.example.randomalarm.contract.MainContract;
import com.example.randomalarm.setting.AlarmSettingInfo;

import java.util.List;

/**
 * author : Mzz
 * date : 2019 2019/4/27 17:02
 * description :
 */
public class MainAdapter extends BaseQuickAdapter <AlarmSettingInfo, BaseViewHolder> {

    public MainAdapter(int layoutResId, @Nullable List <AlarmSettingInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, AlarmSettingInfo alarmSettingInfo) {
        int swhAlarmStatus = R.id.swh_alarm_status;
        viewHolder.setText(R.id.tv_alarm_time, alarmSettingInfo.getShowedTime() + " id:" + alarmSettingInfo.getId())
                .addOnClickListener(swhAlarmStatus);
        viewHolder.setChecked(swhAlarmStatus, alarmSettingInfo.getIsOpenStatus());
    }

    public void setCheckAlarmListener(MainContract.Presenter presenter) {
        this.setOnItemChildClickListener((adapter, view, position) -> {
            Switch alarmStatus = (Switch) view;
            AlarmSettingInfo alarm = (AlarmSettingInfo) adapter.getItem(position);
            if (alarm != null) {
                alarm.setIsOpenStatus(alarmStatus.isChecked());
                presenter.insertOrReplace(alarm);
            }
        });
    }
}
