package com.example.randomalarm.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TimePicker;

import com.example.randomalarm.R;
import com.example.randomalarm.common.ViewerHelper;
import com.example.randomalarm.contract.AlarmSettingContract;
import com.example.randomalarm.model.DefaultSettingModel;
import com.example.randomalarm.presenter.AlarmSettingPresenter;
import com.example.randomalarm.song.AlarmSongActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DefaultSettingActivity extends AppCompatActivity implements AlarmSettingContract.View {

    @BindView(R.id.rv_default_setting)
    RecyclerView rvDefaultSetting;
    DefaultSettingModel defaultSettingModel;
    private String ringName;
    private AlarmSettingContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_setting);
        ButterKnife.bind(this);
        ViewerHelper.displayHomeAsUp(getSupportActionBar());

        ringName = this.getString(R.string.ring_name);
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
        if (itemId == android.R.id.home) {
            this.finish();
            return true;
        } else if (itemId == R.id.action_save) {
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
    public void showSongPathsSetting(AlarmSettingInfo alarmSettingInfo) {
        Intent intent = getNewIntent(AlarmSongActivity.class);
        intent.putExtra(ringName, alarmSettingInfo);
        startActivityForResult(intent, AlarmSettingActivity.RING_SET);
    }

    @Override
    public RecyclerView getRecyclerView() {
        return rvDefaultSetting;
    }

    public Intent getNewIntent(Class <?> cls) {
        return new Intent(DefaultSettingActivity.this, cls);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == AlarmSettingActivity.RING_SET && data != null) {
            AlarmSettingInfo alarmSettingInfo = data.getParcelableExtra(ringName);
            presenter.setAlarmSettingInfo(alarmSettingInfo);
        }
    }

    @Override
    public void setPresenter(AlarmSettingContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public Context getContext() {
        return DefaultSettingActivity.this;
    }

}
