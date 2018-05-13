package com.example.graeme.beamitup;

import android.app.Application;

import com.example.graeme.beamitup.eth.DaoMaster;
import com.example.graeme.beamitup.eth.DaoSession;

import org.greenrobot.greendao.database.Database;

public class BeamItUp extends Application {

    private DaoSession daoSession;

    @Override
    public void onCreate(){
        super.onCreate();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "eth-db", null);
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession(){
        return daoSession;
    }

}
