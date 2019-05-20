package com.example.randomalarm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.randomalarm.adapter.MainAdapter;
import com.example.randomalarm.alarm.SurpriseAlarm;
import com.example.randomalarm.common.EventBusHelper;
import com.example.randomalarm.contract.MainContract;
import com.example.randomalarm.edit.AlarmEditActivity;
import com.example.randomalarm.edit.EditItemInfo;
import com.example.randomalarm.model.AlarmSettingModel;
import com.example.randomalarm.presenter.MainPresenter;
import com.example.randomalarm.setting.AlarmSettingActivity;
import com.example.randomalarm.setting.AlarmSettingInfo;
import com.example.randomalarm.setting.AppSettingActivity;
import com.example.randomalarm.setting.DefaultSettingActivity;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    public static final int INSERT_OR_REPLACE_ALARM = 2;
    @BindView(R.id.rv_alarm)
    RecyclerView rvAlarm;
    @BindView(R.id.fab_alarm_add)
    FloatingActionButton fabAdd;
    MainAdapter mainAdapter;
    List <AlarmSettingInfo> alarmSettingInfos;
    SurpriseAlarm surpriseAlarm;
    private MainContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        presenter = new MainPresenter(this);
        presenter.initOrRefreshAlarm();
        EventBusHelper.register(this);
//        Intent intent1 = new Intent(this, AlarmInitService.class);
//        this.startService(intent1);
    }

    @Override
    public void onDestroy() {
        EventBusHelper.unregister(this);
        if (surpriseAlarm != null) {
            surpriseAlarm.close();
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_default_setting:
                Intent intent = getNewIntent(DefaultSettingActivity.class);
                startActivity(intent);
                break;
            case R.id.action_edit:
                showEditActivity();
                break;
            case R.id.action_app_setting:
                Intent intent1 = getNewIntent(AppSettingActivity.class);
                startActivity(intent1);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showEditActivity() {
        ArrayList <EditItemInfo> editData = new ArrayList <>();
        for (AlarmSettingInfo settingInfo : alarmSettingInfos) {
            editData.add(new EditItemInfo(settingInfo.getId().intValue(), settingInfo.getShowedTime()));
        }
        Intent intent = getNewIntent(AlarmEditActivity.class);
        intent.putParcelableArrayListExtra(AlarmEditActivity.EDIT_DATA, editData);
        startActivityForResult(intent, AlarmEditActivity.EDIT_SAVE);
    }

    /**
     * 添加闹钟
     *
     * @param view
     */
    @OnClick(R.id.fab_alarm_add)
    public void addAlarm_onClick(View view) {
        AlarmSettingInfo alarmSettingInfo = presenter.getNewAlarm();
        showAlarmSetting(alarmSettingInfo);
    }

    private void showAlarmSetting(AlarmSettingInfo alarmSettingInfo) {
        Intent intent = getNewIntent(AlarmSettingActivity.class);
        intent.putExtra(AlarmSettingActivity.ALARM_SETTING_INFO, alarmSettingInfo);
        startActivityForResult(intent, INSERT_OR_REPLACE_ALARM);
    }

    private Intent getNewIntent(Class <?> cls) {
        return new Intent(MainActivity.this, cls);
    }

    private void initAlarm() {
        mainAdapter = new MainAdapter(R.layout.item_alarm, alarmSettingInfos);
        setOnItemClickListener();
        mainAdapter.setCheckAlarmListener(presenter);
        rvAlarm.setLayoutManager(new LinearLayoutManager(this));
        rvAlarm.setAdapter(mainAdapter);
    }

    @Override
    public void initOrRefreshAlarm(List <AlarmSettingInfo> alarmSettingInfos) {
        this.alarmSettingInfos = alarmSettingInfos;
        if (mainAdapter != null) {
            mainAdapter.setNewData(alarmSettingInfos);
        } else {
            initAlarm();
        }
    }

    @Override
    public Context getContext() {
        return MainActivity.this;
    }

    public void setOnItemClickListener() {
        mainAdapter.setOnItemClickListener((adapter, view, position) -> {
            AlarmSettingInfo alarm = (AlarmSettingInfo) adapter.getItem(position);
            if (alarm != null) {
                showAlarmSetting(alarm);
            }
        });
        mainAdapter.setOnItemLongClickListener((adapter, view, position) -> {
            showEditActivity();
            return false;
        });
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == AlarmEditActivity.EDIT_SAVE) {
            onSaveEditEvent(data);
        }
    }

    public void onSaveEditEvent(Intent data) {
        ArrayList <Integer> deleteNums = data.getIntegerArrayListExtra(AlarmEditActivity.DELETE_NUM);
        if (deleteNums == null) {
            return;
        }
        ArrayList <Long> ids = AlarmSettingModel.integerToLongList(deleteNums);
        presenter.deleteByKey(ids);
    }

    @Subscribe
    public void onInsertOrReplaceAlarmEvent(AlarmSettingInfo alarmSettingInfo) {
        if (surpriseAlarm == null) {
            surpriseAlarm = new SurpriseAlarm(this);
        }
        surpriseAlarm.showSurprise(alarmSettingInfo.getHour(), alarmSettingInfo.getMinute());
        presenter.insertOrReplace(alarmSettingInfo);
        presenter.initOrRefreshAlarm();
    }

}
