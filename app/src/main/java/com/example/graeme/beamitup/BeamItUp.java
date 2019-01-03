package com.example.graeme.beamitup;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.graeme.beamitup.listener.TransactionNotification;
import com.example.graeme.beamitup.listener.TransactionNotificationManager;
import com.example.graeme.beamitup.listener.TransferClient;
import com.example.graeme.beamitup.wallet.DaoMaster;
import com.example.graeme.beamitup.wallet.DaoSession;
import com.example.graeme.beamitup.wallet.Wallet;

import org.greenrobot.greendao.database.Database;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.websocket.WebSocketService;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeamItUp extends Application {
    private static final String TAG = "BeamItUp";

    private static final boolean INCLUDE_RAW_RESPONSES = false;
    private DaoSession daoSession;
    static public final String INFURA_URL = "wss://rinkeby.infura.io/ws";
    private Web3j web3j;

    private Map<String, TransactionNotification> notifications;

    @Override
    public void onCreate(){
        super.onCreate();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "wallet-db", null);
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
        WebSocketService webSocketService = new WebSocketService(INFURA_URL, INCLUDE_RAW_RESPONSES);
        try {
            webSocketService.connect();
        } catch (ConnectException e) {
            e.printStackTrace();
        }
        web3j = Web3j.build(webSocketService);
        createNotificationChannel();

        new TransactionNotificationManager(
                daoSession.getWalletDao().loadAll(),
                web3j,
                getApplicationContext()
        );
    }

    public Web3j getWeb3j() {
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
