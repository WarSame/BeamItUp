package com.example.graeme.beamitup.wallet;

import android.app.IntentService;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.graeme.beamitup.BeamItUp;
import encryption.Encryption;
import encryption.Encryptor;

import com.example.graeme.beamitup.R;

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
                .setContentTitle("Creating wallet")
                .setContentText(nickname)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setProgress(0, 0, true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(id, builder.build());

        String longPassword = Encryption.generateLongRandomString();
        try {
            File walletDir = Wallet.getWalletDir(this);
            String walletName = Wallet.generateWallet(longPassword, walletDir);
            Wallet wallet = handleWalletCreation(walletName, nickname, longPassword);
            onCreateWalletSuccess(wallet);
        } catch (Exception e) {
            e.printStackTrace();
            onCreateWalletFail();
        }
    }

    public GenerateWalletService(){
        super("GenerateWalletService");
    }

    private Wallet handleWalletCreation(String walletName, String nickname, String longPassword) throws Exception{
        File walletFile = Wallet.getWalletFile(this, walletName);
        Credentials credentials = Wallet.retrieveCredentials(walletFile, longPassword);

        Encryptor encryptor = new Encryptor()
                .encryptWalletPassword(walletName, longPassword);

        Wallet wallet = new Wallet.WalletBuilder()
            .nickname(nickname)
            .address(credentials.getAddress())
            .walletName(walletName)
            .encryptedLongPassword(encryptor.getEncryptedLongPassword())
            .IV(encryptor.getIV())
            .build();

        insertWallet(wallet);
        return wallet;
    }

    private void insertWallet(Wallet wallet){
        Log.i(TAG, "Inserting new wallet");
        DaoSession daoSession = ((BeamItUp)getApplication()).getDaoSession();
        WalletDao walletDao = daoSession.getWalletDao();
        walletDao.insert(wallet);
        Log.i(TAG, "Inserted new wallet " + wallet.getId());
    }

    private void onCreateWalletSuccess(Wallet wallet){
        Log.i(TAG, "Wallet created");
        Intent viewWalletIntent = new Intent(this, WalletDetailActivity.class);
        viewWalletIntent.putExtra("wallet", wallet);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this)
                .addNextIntentWithParentStack(viewWalletIntent);

        PendingIntent viewWalletPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "BeamItUp")
                .setContentTitle("Wallet created")
                .setContentText(wallet.getNickname())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(viewWalletPendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(id, builder.build());
    }

    private void onCreateWalletFail(){
        Log.i(TAG, "Wallet creation failed");
    }

}
