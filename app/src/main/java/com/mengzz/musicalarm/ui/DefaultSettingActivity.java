package com.mengzz.musicalarm.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TimePicker;

import com.mengzz.musicalarm.R;
import com.mengzz.musicalarm.contract.AlarmSettingContract;
import com.mengzz.musicalarm.model.DefaultSettingModel;
import com.mengzz.musicalarm.multicalendar.MultCalendarActivity;
import com.mengzz.musicalarm.presenter.AlarmSettingPresenter;
import com.mengzz.musicalarm.setting.AlarmSettingInfo;
import com.mzz.zandroidcommon.view.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DefaultSettingActivity extends BaseActivity implements AlarmSettingContract.View {

    @BindView(R.id.rv_default_setting)
    RecyclerView rvDefaultSetting;
    DefaultSettingModel defaultSettingModel;
    private AlarmSettingContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_setting);
        ButterKnife.bind(this);

        defaultSettingModel = new DefaultSettingModel(this);
        presenter = new AlarmSettingPresenter(this, defaultSettingModel.loadDefaultAlarmSetting());
        presenter.initAlarm();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_save) {
            presenter.saveAlarmSetting();
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 保存
     */
    @Override
    public void save(AlarmSettingInfo alarmSettingInfo) {
        defaultSettingModel.insertOrReplace(alarmSettingInfo);
    }

    @Override
    public TimePicker getTimePicker() {
        return null;
    }

    @Override
    public void showSongPathsActivity(AlarmSettingInfo alarmSettingInfo) {
        AlarmSongActivity.startForResult(this, alarmSettingInfo);
    }

    @Override
    public void showMultCalendarActivity(AlarmSettingInfo alarmSettingInfo) {
        MultCalendarActivity.startForResult(this, alarmSettingInfo.getAlarmCalendars());
    }

    @Override
    public RecyclerView getRecyclerView() {
        return rvDefaultSetting;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.saveSetting(resultCode, data);
    }

}
