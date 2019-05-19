package com.example.randomalarm.presenter;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.TextView;

import com.example.randomalarm.adapter.MultipleItem;
import com.example.randomalarm.common.StringHelper;
import com.example.randomalarm.contract.AlarmSettingContract;
import com.example.randomalarm.contract.DefaultSettingContract;
import com.example.randomalarm.setting.DefaultSetting;
import com.example.randomalarm.setting.MatisseHelper;

import java.util.List;

/**
 * author : Mzz
 * date : 2019 2019/5/19 19:04
 * description :
 */
public class DefaultSettingPresenter extends AlarmSettingPresenter implements DefaultSettingContract.Presenter {
    private static final String RemindImagePath = "提醒背景图片";
    private final DefaultSetting defaultSetting;
    private AlarmSettingContract.View view;

    public DefaultSettingPresenter(AlarmSettingContract.View view, DefaultSetting defaultSetting) {
        super(view, defaultSetting.getAlarmSettingInfo());
        this.view = view;
        this.defaultSetting = defaultSetting;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void initAlarm() {
        List <MultipleItem> settings = super.getSettings();
        settings.add(new MultipleItem(MultipleItem.RIGHT_BUTTON, RemindImagePath));
        super.initAdapter(settings);
    }

    @Override
    public void showSetting(int position, TextView textView) {
        if (position == 6) {
        } else {
            super.showSetting(position, textView);
        }
    }

    private String getRemindImagePathInfo() {
        return StringHelper.getLocalFormat("%s", RemindImagePath);

    }
}
