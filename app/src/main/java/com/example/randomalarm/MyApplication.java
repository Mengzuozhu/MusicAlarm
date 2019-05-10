package com.example.randomalarm;

import android.app.Application;

import com.example.randomalarm.greendao.db.DaoMaster;
import com.example.randomalarm.greendao.db.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * author : Mzz
 * date : 2019 2019/4/26 21:22
 * description :
 */
public class MyApplication extends Application {
    private static DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, this.getString(R.string.db_name));
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }
}
