package com.mengzz.musicalarm.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TimePicker;

import com.mengzz.musicalarm.R;
import com.mengzz.musicalarm.contract.AlarmSettingContract;
import com.mengzz.musicalarm.multicalendar.MultCalendarActivity;
import com.mengzz.musicalarm.presenter.AlarmSettingPresenter;
import com.mengzz.musicalarm.setting.AlarmSettingInfo;
import com.mzz.zandroidcommon.view.BaseActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
public class AlarmSettingActivity extends BaseActivity implements AlarmSettingContract.View {

    public static final String ALARM_SETTING_INFO = "ALARM_SETTING_INFO";
    private static final int ALARM_SETTING_INFO_CODE = 2;
    @BindView(R.id.tp_setting)
    TimePicker timePicker;
    @BindView(R.id.rv_setting)
    RecyclerView rvSetting;
    private AlarmSettingContract.Presenter presenter;

    /**
     * Start for result.
     *
     * @param activity the activity
     * @param value    the value
     */
    public static void startForResult(FragmentActivity activity, Parcelable value) {
        Intent intent =
                new Intent(activity, AlarmSettingActivity.class).putExtra(ALARM_SETTING_INFO,
                        value);
        activity.startActivityForResult(intent, ALARM_SETTING_INFO_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setting);
        ButterKnife.bind(this);

        timePicker.setIs24HourView(true);
        AlarmSettingInfo alarmSettingInfo = getParcelableExtra(ALARM_SETTING_INFO);
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
        EventBus.getDefault().post(alarmSettingInfo);
    }

    @Override
    public TimePicker getTimePicker() {
        return timePicker;
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
        return rvSetting;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.saveSetting(resultCode, data);
    }

}
