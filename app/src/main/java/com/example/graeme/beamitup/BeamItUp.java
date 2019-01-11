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
import com.example.graeme.beamitup.wallet.DaoMaster;
import com.example.graeme.beamitup.wallet.DaoSession;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.greenrobot.greendao.database.Database;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.websocket.WebSocketService;

import java.net.ConnectException;
import java.security.Provider;
import java.security.Security;
import java.util.Map;

public class BeamItUp extends Application {
    private static final String TAG = "BeamItUp";

    private static final boolean INCLUDE_RAW_RESPONSES = false;
    private DaoSession daoSession;
    static public final String INFURA_URL = "wss://rinkeby.infura.io/ws";
    private Web3j web3j;
    public static final long[] SUCCESS_VIBRATE_PATTERN = new long[]{0, 200, 100, 200};
    public static final long[] FAILURE_VIBRATE_PATTERN = new long[]{0, 50, 25, 50};
    public static final long[] START_VIBRATE_PATTERN = new long[]{0, 100};

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

        setupBouncyCastle();
    }

    //Avoid BC problems on certain API levels
    private void setupBouncyCastle(){
        final Provider provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME);
        if (provider == null) {
            // Web3j will set up the provider lazily when it's first used.
            return;
        }
        if (provider.getClass().equals(BouncyCastleProvider.class)) {
            // BC with same package name, shouldn't happen in real life.
            return;
        }
        // Android registers its own BC provider. As it might be outdated and might not include
        // all needed ciphers, we substitute it with a known BC bundled in the app.
        // Android's BC has its package rewritten to "com.android.org.bouncycastle" and because
        // of that it's possible to have another BC implementation loaded in VM.
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME);
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
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
