package com.example.graeme.beamitup;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.example.graeme.beamitup.listener.TransferClient;
import com.example.graeme.beamitup.wallet.DaoMaster;
import com.example.graeme.beamitup.wallet.DaoSession;
import com.example.graeme.beamitup.wallet.Wallet;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.greenrobot.greendao.database.Database;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.websocket.WebSocketService;

import java.net.ConnectException;
import java.security.Provider;
import java.security.Security;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        createNotificationChannel();
        this.web3j = createWeb3j();
        this.daoSession = createDaoSession();
        createTransferClient();
        setupBouncyCastle();
    }

    private Web3j createWeb3j(){
        WebSocketService webSocketService = new WebSocketService(INFURA_URL, INCLUDE_RAW_RESPONSES);
        try {
            webSocketService.connect();
        } catch (ConnectException e) {
            e.printStackTrace();
        }
        return Web3j.build(webSocketService);
    }

    private DaoSession createDaoSession(){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "wallet-db", null);
        Database db = helper.getWritableDb();
        return new DaoMaster(db).newSession();
    }

    private TransferClient createTransferClient(){
        List<Wallet> wallets = daoSession.getWalletDao().loadAll();
        Set<String> addresses = new HashSet<>();
        for (Wallet wallet: wallets){
            addresses.add(wallet.getAddress());
        }

        return new TransferClient(
                web3j,
                addresses,
                getApplicationContext()
        );
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
