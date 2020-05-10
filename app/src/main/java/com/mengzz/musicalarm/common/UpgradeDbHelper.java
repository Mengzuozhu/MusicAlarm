package com.mengzz.musicalarm.common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.mengzz.musicalarm.greendao.db.AlarmSettingInfoDao;
import com.mengzz.musicalarm.greendao.db.DaoMaster;
import com.mzz.zandroidcommon.common.MigrationDbHelper;

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
//            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " +
//            newVersion + " by migrating all tables data");
            MigrationDbHelper.migrate(db, AlarmSettingInfoDao.class);
        }
    }
}
