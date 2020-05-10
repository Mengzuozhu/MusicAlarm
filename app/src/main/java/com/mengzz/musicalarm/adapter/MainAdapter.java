package com.mengzz.musicalarm.adapter;

import androidx.annotation.Nullable;
import android.widget.Switch;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mengzz.musicalarm.R;
import com.mengzz.musicalarm.contract.MainContract;
import com.mengzz.musicalarm.setting.AlarmSettingInfo;

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
        int swhAlarmStatus = R.id.swh_status;
        viewHolder.setText(R.id.tv_alarm_time, alarmSettingInfo.getShowedTime()).addOnClickListener(swhAlarmStatus);
        viewHolder.setChecked(swhAlarmStatus, alarmSettingInfo.getIsOpenStatus());
    }

    public void setCheckAlarmListener(MainContract.Presenter presenter) {
        this.setOnItemChildClickListener((adapter, view, position) -> {
            Switch switchStatus = (Switch) view;
            AlarmSettingInfo alarm = (AlarmSettingInfo) adapter.getItem(position);
            if (alarm != null && switchStatus != null) {
                alarm.setIsOpenStatus(switchStatus.isChecked());
                presenter.insertOrReplace(alarm);
            }
        });
    }
}
