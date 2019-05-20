package com.example.randomalarm.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TimePicker;

import com.example.randomalarm.R;
import com.example.randomalarm.common.EventBusHelper;
import com.example.randomalarm.common.ViewerHelper;
import com.example.randomalarm.contract.AlarmSettingContract;
import com.example.randomalarm.multiCalendar.MultCalendarActivity;
import com.example.randomalarm.presenter.AlarmSettingPresenter;
import com.example.randomalarm.song.AlarmSongActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
public class AlarmSettingActivity extends AppCompatActivity implements AlarmSettingContract.View {

    public static final int RING_SET_CODE = 4;
    public static final String ALARM_SETTING_INFO = "AlarmSettingInfo";
    @BindView(R.id.tp_setting)
    TimePicker timePicker;
    @BindView(R.id.rv_setting)
    RecyclerView rvSetting;
    private String ringName;
    private AlarmSettingContract.Presenter presenter;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setting);
        ButterKnife.bind(this);
        ViewerHelper.displayHomeAsUp(getSupportActionBar());

        timePicker.setIs24HourView(true);
        ringName = this.getString(R.string.ring_name);
        Intent intent = getIntent();
        AlarmSettingInfo alarmSettingInfo = intent.getParcelableExtra(ALARM_SETTING_INFO);
        presenter = new AlarmSettingPresenter(this, alarmSettingInfo);
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
        EventBus.getDefault().post(alarmSettingInfo);
    }

    @Override
    public TimePicker getTimePicker() {
        return timePicker;
    }

    @Override
    public void showSongPathsActivity(AlarmSettingInfo alarmSettingInfo) {
        Intent intent = getNewIntent(AlarmSongActivity.class);
        intent.putExtra(ringName, alarmSettingInfo);
        startActivityForResult(intent, RING_SET_CODE);
    }

    @Override
    public void showMultCalendarActivity(AlarmSettingInfo alarmSettingInfo) {
        Intent intent = getNewIntent(MultCalendarActivity.class);
        intent.putParcelableArrayListExtra(MultCalendarActivity.ALARM_CALENDAR_DATA, alarmSettingInfo.getAlarmCalendars());
        startActivityForResult(intent, MultCalendarActivity.MULT_CALENDAR_CODE);
    }

    @Override
    public RecyclerView getRecyclerView() {
        return rvSetting;
    }

    public Intent getNewIntent(Class <?> cls) {
        return new Intent(AlarmSettingActivity.this, cls);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (resultCode == RING_SET_CODE) {
            AlarmSettingInfo alarmSettingInfo = data.getParcelableExtra(ringName);
            presenter.setAlarmSettingInfo(alarmSettingInfo);
        }else if (resultCode == MultCalendarActivity.MULT_CALENDAR_CODE) {
            ArrayList <AlarmCalendar> selectAlarmCalendar = data.getParcelableArrayListExtra(MultCalendarActivity.ALARM_CALENDAR_DATA);
            presenter.setAlarmCalendar(selectAlarmCalendar);
        }
    }

    @Override
    public void setPresenter(AlarmSettingContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public Context getContext() {
        return AlarmSettingActivity.this;
    }

}
