package com.mengzz.musicalarm.model;

import android.content.Context;

import com.mengzz.musicalarm.R;
import com.mengzz.musicalarm.common.UpgradeDbHelper;
import com.mengzz.musicalarm.greendao.db.AlarmSettingInfoDao;
import com.mengzz.musicalarm.greendao.db.DaoMaster;
import com.mengzz.musicalarm.greendao.db.DaoSession;
import com.mengzz.musicalarm.setting.AlarmRepeatMode;
import com.mengzz.musicalarm.setting.AlarmSettingInfo;
import com.mzz.zandroidcommon.common.DateHelper;

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
        UpgradeDbHelper helper = new UpgradeDbHelper(context,
                context.getString(R.string.default_alarm_setting_db_name));
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
        alarmSettingInfoDao = daoSession.getAlarmSettingInfoDao();
    }

    public AlarmSettingInfo loadDefaultAlarmSetting() {
        List <AlarmSettingInfo> data = daoSession.loadAll(AlarmSettingInfo.class);
        AlarmSettingInfo defaultSetting;
        if (data != null && !data.isEmpty()) {
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
