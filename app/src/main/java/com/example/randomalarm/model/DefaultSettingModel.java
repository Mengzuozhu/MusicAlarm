package com.example.randomalarm.model;

import android.content.Context;

import com.example.randomalarm.R;
import com.example.randomalarm.common.DateHelper;
import com.example.randomalarm.common.UpgradeDbHelper;
import com.example.randomalarm.greendao.db.AlarmSettingInfoDao;
import com.example.randomalarm.greendao.db.DaoMaster;
import com.example.randomalarm.greendao.db.DaoSession;
import com.example.randomalarm.setting.AlarmRepeatMode;
import com.example.randomalarm.setting.AlarmSettingInfo;

import org.greenrobot.greendao.database.Database;

import java.util.List;

/**
 * author : Mzz
 * date : 2019 2019/5/11 15:33
 * description :
 */
public class DefaultSettingModel {
    private DaoSession daoSession;
    private AlarmSettingInfoDao alarmSettingInfoDao;

    public DefaultSettingModel(Context context) {
        UpgradeDbHelper helper = new UpgradeDbHelper(context, context.getString(R.string.default_alarm_setting_db_name));
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
        alarmSettingInfoDao = daoSession.getAlarmSettingInfoDao();
    }

    public AlarmSettingInfo loadDefaultAlarmSetting() {
        List <AlarmSettingInfo> data = daoSession.loadAll(AlarmSettingInfo.class);
        AlarmSettingInfo defaultSetting;
        if (data != null && data.size() > 0) {
            defaultSetting = data.get(0);
        } else {
            defaultSetting = new AlarmSettingInfo();
            defaultSetting.setAlarmRepeatMode(AlarmRepeatMode.toArrayList());
        }
        defaultSetting.setHour(DateHelper.getNowHour());
        defaultSetting.setMinute(DateHelper.getNowMinute());
        return defaultSetting;
    }

    public void insertOrReplace(AlarmSettingInfo alarmSettingInfo) {
        if (alarmSettingInfo == null) {
            return;
        }
        alarmSettingInfoDao.insertOrReplace(alarmSettingInfo);
    }

}
