package com.example.randomalarm.model;

import com.example.randomalarm.MyApplication;
import com.example.randomalarm.contract.AlarmSettingContract;
import com.example.randomalarm.greendao.db.AlarmSettingInfoDao;
import com.example.randomalarm.greendao.db.DaoSession;
import com.example.randomalarm.setting.AlarmSettingInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * author : Mzz
 * date : 2019 2019/5/2 19:58
 * description :
 */
public class AlarmSettingModel implements AlarmSettingContract.Model {
    private static DaoSession daoSession;
    private static AlarmSettingInfoDao alarmSettingInfoDao;

    static {
        daoSession = MyApplication.getDaoSession();
        alarmSettingInfoDao = daoSession.getAlarmSettingInfoDao();
    }

    public static List <AlarmSettingInfo> getSortedAlarmInfos() {
        List <AlarmSettingInfo> data = daoSession.loadAll(AlarmSettingInfo.class);
        data.sort((o1, o2) -> o1.getId().compareTo(o2.getId()));
        return data;
    }

    public static void deleteByKey(Iterable <Long> keys) {
        alarmSettingInfoDao.deleteByKeyInTx(keys);
    }

    public static void insertOrReplace(AlarmSettingInfo alarmSettingInfo) {
        if (alarmSettingInfo == null) {
            return;
        }
        alarmSettingInfoDao.insertOrReplace(alarmSettingInfo);
    }

    public static void update(AlarmSettingInfo alarmSettingInfo) {
        if (alarmSettingInfo == null) {
            return;
        }
        alarmSettingInfoDao.update(alarmSettingInfo);
    }

    public static ArrayList <Long> integerToLongList(ArrayList <Integer> deleteNum) {
        ArrayList <Long> ids = new ArrayList <>();
        for (Integer i : deleteNum) {
            ids.add(i.longValue());
        }
        return ids;
    }

    public static AlarmSettingInfo getAlarmSettingInfoById(Object key) {
        return daoSession.load(AlarmSettingInfo.class, key);
    }
}
