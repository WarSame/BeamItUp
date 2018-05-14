package com.example.graeme.beamitup;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.example.graeme.beamitup.eth.DaoMaster;
import com.example.graeme.beamitup.eth.DaoSession;

import org.greenrobot.greendao.database.Database;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.http.HttpService;

public class BeamItUp extends Application {

    private DaoSession daoSession;
    static private final String INFURA_URL = "https://rinkeby.infura.io/SxLC8uFzMPfzwnlXHqx9";
    static private Web3j web3j;

    @Override
    public void onCreate(){
        super.onCreate();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "eth-db", null);
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
        web3j = buildWeb3j();
        createNotificationChannel();
    }

    private static Web3j buildWeb3j(){
        return Web3jFactory.build(
                new HttpService(INFURA_URL)
        );
    }

    public static Web3j getWeb3j() {
        return web3j;
    }

    public DaoSession getDaoSession(){
        return daoSession;
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            String CHANNEL_ID = "BeamItUp";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription("Generate Wallet");

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}
