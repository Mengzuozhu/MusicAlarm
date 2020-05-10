package com.mengzz.musicalarm;

import android.app.Application;

import com.mengzz.musicalarm.common.UpgradeDbHelper;
import com.mengzz.musicalarm.greendao.db.DaoMaster;
import com.mengzz.musicalarm.greendao.db.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * author : Mzz
 * date : 2019 2019/4/26 21:22
 * description :
 */
public class MyApplication extends Application {
    private static DaoSession daoSession;

    public static DaoSession getDaoSession() {
        return daoSession;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        UpgradeDbHelper helper = new UpgradeDbHelper(this,
                this.getString(R.string.alarm_setting_db_name));
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }
}
