package com.bwei.ssp.multithreading.utlis;

import android.app.Application;

import com.bwei.ssp.multithreading.greenDao.DaoMaster;
import com.bwei.ssp.multithreading.greenDao.DaoSession;
import com.bwei.ssp.multithreading.greenDao.UserDao;

/**
 * Created by lenovo on 2018/2/22.
 */

public class App extends Application {
    public   static UserDao userDao;
    @Override
    public void onCreate() {
        super.onCreate();
        DaoMaster.DevOpenHelper  devOpenHelper = new DaoMaster.DevOpenHelper(this,"ssptoday.db",null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        DaoSession daoSession = daoMaster.newSession();
        userDao = daoSession.getUserDao();
    }
}
