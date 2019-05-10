package com.example.randomalarm.greendao.db;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.example.randomalarm.setting.AlarmSettingInfo;

import com.example.randomalarm.greendao.db.AlarmSettingInfoDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig alarmSettingInfoDaoConfig;

    private final AlarmSettingInfoDao alarmSettingInfoDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        alarmSettingInfoDaoConfig = daoConfigMap.get(AlarmSettingInfoDao.class).clone();
        alarmSettingInfoDaoConfig.initIdentityScope(type);

        alarmSettingInfoDao = new AlarmSettingInfoDao(alarmSettingInfoDaoConfig, this);

        registerDao(AlarmSettingInfo.class, alarmSettingInfoDao);
    }
    
    public void clear() {
        alarmSettingInfoDaoConfig.clearIdentityScope();
    }

    public AlarmSettingInfoDao getAlarmSettingInfoDao() {
        return alarmSettingInfoDao;
    }

}
