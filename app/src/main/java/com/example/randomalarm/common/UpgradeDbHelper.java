package com.example.randomalarm.common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.randomalarm.greendao.db.AlarmSettingInfoDao;
import com.example.randomalarm.greendao.db.DaoMaster;
import com.example.randomalarm.greendao.db.DefaultSettingDao;

/**
 * 升级greendao数据库
 * author : Mzz
 * date : 2019 2019/5/18 15:41
 * description :
 */
public class UpgradeDbHelper extends DaoMaster.OpenHelper {

    public UpgradeDbHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion < newVersion) {
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by migrating all tables data");
            MigrationHelper.migrate(db, AlarmSettingInfoDao.class, DefaultSettingDao.class);
        }
    }
}
