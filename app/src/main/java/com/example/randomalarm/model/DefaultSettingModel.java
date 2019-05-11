package com.example.randomalarm.model;

import android.content.Context;

import com.example.randomalarm.R;
import com.example.randomalarm.common.DateHelper;
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
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context,
                context.getString(R.string.default_alarm_setting_db_name));
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
        alarmSettingInfoDao = daoSession.getAlarmSettingInfoDao();
    }

    public AlarmSettingInfo load() {
        List <AlarmSettingInfo> data = daoSession.loadAll(AlarmSettingInfo.class);
        if (data != null && data.size() > 0) {
            return data.get(0);
        } else {
            return getDefaultAlarmSetting();
        }
    }

    private AlarmSettingInfo getDefaultAlarmSetting() {
        AlarmSettingInfo alarmSettingInfo = new AlarmSettingInfo();
        alarmSettingInfo.setId(0L);
        alarmSettingInfo.setHour(DateHelper.getNowHour());
        alarmSettingInfo.setMinute(DateHelper.getNowMinute());
        alarmSettingInfo.setAlarmRepeatMode(AlarmRepeatMode.toArrayList());
        return alarmSettingInfo;
    }

    public void insertOrReplace(AlarmSettingInfo alarmSettingInfo) {
        if (alarmSettingInfo == null) {
            return;
        }
        alarmSettingInfoDao.insertOrReplace(alarmSettingInfo);
    }

}