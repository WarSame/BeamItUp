package com.example.graeme.beamitup.eth_tasks;

import android.app.IntentService;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.graeme.beamitup.BeamItUp;
import com.example.graeme.beamitup.Encryption;
import com.example.graeme.beamitup.R;
import com.example.graeme.beamitup.eth.DaoSession;
import com.example.graeme.beamitup.eth.Eth;
import com.example.graeme.beamitup.eth.EthDao;
import com.example.graeme.beamitup.eth.EthDetailActivity;
import com.example.graeme.beamitup.wallet.EncryptedWallet;
import com.example.graeme.beamitup.wallet.WalletHelper;

import org.web3j.crypto.Credentials;

import java.io.File;

public class GenerateWalletService extends IntentService {
    private static final String TAG = "GenerateWalletService";

    private int id = (int)System.currentTimeMillis();

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null){
            Log.e(TAG, "Null intent received");
            return;
        }

        String nickname = intent.getStringExtra("nickname");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "BeamItUp")
                .setContentTitle("Creating eth")
                .setContentText(nickname)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setProgress(0, 0, true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(id, builder.build());

        String longPassword = Encryption.generateLongRandomString();
        try {
            File walletDir = WalletHelper.getWalletDir(this);
            String walletName = WalletHelper.generateWallet(longPassword, walletDir);
            Eth eth = handleWalletCreation(walletName, nickname, longPassword);
            onCreateEthSuccess(eth);
        } catch (Exception e) {
            e.printStackTrace();
            onCreateEthFail();
        }
    }

    public GenerateWalletService(){
        super("GenerateWalletService");
    }

    private Eth handleWalletCreation(String walletName, String nickname, String longPassword) throws Exception{
        File walletFile = WalletHelper.getWalletFile(this, walletName);
        Credentials credentials = WalletHelper.retrieveCredentials(walletFile, longPassword);

        Encryption encryption = new Encryption();
        Encryption.Encryptor encryptor = encryption.new Encryptor()
                .encryptWalletPassword(walletName, longPassword);

        Eth eth = new Eth()
            .setNickname(nickname)
            .setAddress(credentials.getAddress())
            .setWalletName(walletName)
            .setEncryptedLongPassword(encryptor.getEncryptedLongPassword())
            .setIV(encryptor.getIV());

        insertEth(eth);
        return eth;
    }

    private void insertEth(Eth eth){
        DaoSession daoSession = ((BeamItUp)getApplication()).getDaoSession();
        EthDao ethDao = daoSession.getEthDao();
        ethDao.insert(eth);
        Log.d(TAG, "Inserted new eth " + eth.getId());
    }

    private void onCreateEthSuccess(Eth eth){
        Log.i(TAG, "Eth created");
        Intent viewEthIntent = new Intent(this, EthDetailActivity.class);
        viewEthIntent.putExtra("eth", eth);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this)
                .addNextIntentWithParentStack(viewEthIntent);

        PendingIntent viewEthPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "BeamItUp")
                .setContentTitle("Eth created")
                .setContentText(eth.getNickname())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(viewEthPendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(id, builder.build());
    }

    private void onCreateEthFail(){
        Log.i(TAG, "Eth creation failed");
    }

}
