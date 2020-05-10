package com.mengzz.musicalarm;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mengzz.musicalarm.alarm.SurprisedAlarm;
import com.mengzz.musicalarm.contract.MainContract;
import com.mengzz.musicalarm.ui.LinearEditActivity;
import com.mengzz.musicalarm.model.AlarmSettingModel;
import com.mengzz.musicalarm.presenter.MainPresenter;
import com.mengzz.musicalarm.setting.AlarmSettingInfo;
import com.mengzz.musicalarm.ui.AppSettingActivity;
import com.mengzz.musicalarm.ui.DefaultSettingActivity;
import com.mzz.zandroidcommon.common.EventBusHelper;
import com.mzz.zandroidcommon.view.BaseActivity;
import com.mzz.zandroidcommon.view.ViewerHelper;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements MainContract.View {

    @BindView(R.id.rv_alarm)
    RecyclerView rvAlarm;
    SurprisedAlarm surprisedAlarm;
    private MainContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ViewerHelper.displayHomeAsUpOrNot(getSupportActionBar(), false);

        presenter = new MainPresenter(this);
        presenter.initOrRefreshAlarm();
        EventBusHelper.register(this);
    }

    @Override
    public void onDestroy() {
        EventBusHelper.unregister(this);
        if (surprisedAlarm != null) {
            surprisedAlarm.close();
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        //返回键，不退出程序
        moveTaskToBack(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_default_setting:
                openActivity(DefaultSettingActivity.class);
                break;
            case R.id.action_app_setting:
                openActivity(AppSettingActivity.class);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab_alarm_add)
    public void addAlarmOnClick(View view) {
        presenter.showAlarmSettingActivity(presenter.getNewAlarm());
    }

    @OnClick(R.id.fab_alarm_edit)
    public void editAlarmOnClick(View view) {
        presenter.showEditActivity();
    }

    @Override
    public RecyclerView getRecyclerView() {
        return rvAlarm;
    }

    @Override
    public FragmentActivity getActivity() {
        return this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == LinearEditActivity.EDIT_SAVE) {
            onSaveEditEvent(data);
        }
    }

    public void onSaveEditEvent(Intent data) {
        ArrayList <Integer> deleteIds =
                data.getIntegerArrayListExtra(LinearEditActivity.DELETE_NUM);
        if (deleteIds == null) {
            return;
        }
        List <Long> ids = AlarmSettingModel.integerToLongList(deleteIds);
        presenter.deleteByKey(ids);
    }

    @Subscribe
    public void onInsertOrReplaceAlarmEvent(AlarmSettingInfo alarmSettingInfo) {
        if (alarmSettingInfo == null) {
            return;
        }
        if (surprisedAlarm == null) {
            surprisedAlarm = new SurprisedAlarm(this);
        }
        surprisedAlarm.showSurpriseDialog(alarmSettingInfo);
        presenter.insertOrReplace(alarmSettingInfo);
        presenter.initOrRefreshAlarm();
    }

}
