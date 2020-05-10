package com.mengzz.musicalarm.model;

import com.mengzz.musicalarm.MyApplication;
import com.mengzz.musicalarm.contract.AlarmSettingContract;
import com.mengzz.musicalarm.greendao.db.AlarmSettingInfoDao;
import com.mengzz.musicalarm.greendao.db.DaoSession;
import com.mengzz.musicalarm.setting.AlarmSettingInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * author : Mzz
 * date : 2019 2019/5/2 19:58
 * description :
 */
public class AlarmSettingModel implements AlarmSettingContract.Model {
    private static AlarmSettingInfoDao alarmSettingInfoDao;

    static {
        DaoSession daoSession = MyApplication.getDaoSession();
        alarmSettingInfoDao = daoSession.getAlarmSettingInfoDao();
    }

    private AlarmSettingModel() {
        //no instance
    }

    /**
     * Gets sorted alarm infos.
     *
     * @return the sorted alarm infos
     */
    public static List <AlarmSettingInfo> getSortedAlarmInfos() {
        List <AlarmSettingInfo> data = alarmSettingInfoDao.loadAll();
        data.sort((o1, o2) -> o1.getId().compareTo(o2.getId()));
        return data;
    }

    /**
     * Delete by key.
     *
     * @param keys the keys
     */
    public static void deleteByKey(Iterable <Long> keys) {
        alarmSettingInfoDao.deleteByKeyInTx(keys);
    }

    /**
     * Insert or replace.
     *
     * @param alarmSettingInfo the alarm setting info
     */
    public static void insertOrReplace(AlarmSettingInfo alarmSettingInfo) {
        if (alarmSettingInfo == null) {
            return;
        }
        alarmSettingInfoDao.insertOrReplace(alarmSettingInfo);
    }

    /**
     * Integer to long list list .
     *
     * @param deleteNum the delete num
     * @return the list
     */
    public static List <Long> integerToLongList(List <Integer> deleteNum) {
        ArrayList <Long> ids = new ArrayList <>();
        for (Integer i : deleteNum) {
            ids.add(i.longValue());
        }
        return ids;
    }

    /**
     * Gets alarm setting info by id.
     *
     * @param key the key
     * @return the alarm setting info by id
     */
    public static AlarmSettingInfo getAlarmSettingInfoById(Long key) {
        if (key == -1) {
            return null;
        }
        return alarmSettingInfoDao.load(key);
    }
}
