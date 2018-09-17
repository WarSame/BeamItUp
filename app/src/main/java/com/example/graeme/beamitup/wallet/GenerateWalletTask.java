package com.example.graeme.beamitup.wallet;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.graeme.beamitup.BeamItUp;
import com.example.graeme.beamitup.R;
import com.example.graeme.beamitup.wallet.GenerateWalletService.OnGenerateWallet;
import java.lang.ref.WeakReference;

public class GenerateWalletTask extends AsyncTask<String, Void, Wallet> {
    private static final String TAG = "GenerateWalletTask";
    private final WeakReference<Context> weakContext;
    private boolean isUserAuthenticationRequired;

    private NotificationCompat.Builder builder;
    private NotificationManagerCompat notificationManagerCompat;
    private OnGenerateWallet onGenerateWallet;
    private int notificationID;

    GenerateWalletTask(Context context, boolean isUserAuthenticationRequired, OnGenerateWallet onGenerateWallet){
        this.weakContext = new WeakReference<>(context);
        this.isUserAuthenticationRequired = isUserAuthenticationRequired;
        this.onGenerateWallet = onGenerateWallet;
        this.notificationManagerCompat = NotificationManagerCompat.from(weakContext.get());
        this.notificationID = (int)System.currentTimeMillis();
    }

    @Override
    protected Wallet doInBackground(String... nicknames) {
        String nickname = nicknames[0];
        Log.d(TAG, "Generating wallet for " + nickname);
        builder = new NotificationCompat.Builder(weakContext.get(), "BeamItUp")
                .setContentTitle("Creating wallet")
                .setContentText(nickname)
                .setSmallIcon(R.drawable.ic_beamitup)
                .setProgress(0, 0, true)
                .setVibrate(BeamItUp.START_VIBRATE_PATTERN);

        notificationManagerCompat.notify(notificationID, builder.build());

        Wallet wallet = null;
        try {
            wallet = handleWalletCreation(nickname);
        } catch (Exception e){
            builder
                    .setContentTitle("Wallet creation failed")
                    .setProgress(0,0, false)
                    .setVibrate(BeamItUp.FAILURE_VIBRATE_PATTERN);

            notificationManagerCompat.notify(notificationID, builder.build());
            e.printStackTrace();
        }
        return wallet;
    }

    @Override
    protected void onPostExecute(Wallet wallet){
        onGenerateWallet.onGenerateWallet(wallet);
    }

    private Wallet handleWalletCreation(String nickname) throws Exception{
        Wallet wallet = new Wallet.WalletBuilder()
                .nickname(nickname)
                .context(weakContext.get())
                .isUserAuthenticationRequired(isUserAuthenticationRequired)
                .build();

        insertWallet(wallet);

        onCreateWalletSuccess(wallet);
        return wallet;
    }

    private void insertWallet(Wallet wallet){
        Log.i(TAG, "Inserting new wallet");
        DaoSession daoSession = ((BeamItUp)weakContext.get().getApplicationContext()).getDaoSession();
        WalletDao walletDao = daoSession.getWalletDao();
        walletDao.insert(wallet);
        Log.i(TAG, "Inserted new wallet " + wallet.getId());
    }

    private void onCreateWalletSuccess(Wallet wallet){
        Log.i(TAG, "Wallet created " + wallet.getNickname());
        Intent viewWalletIntent = new Intent(weakContext.get(), WalletDetailActivity.class);
        viewWalletIntent.putExtra("wallet", wallet);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(weakContext.get())
                .addNextIntentWithParentStack(viewWalletIntent);

        PendingIntent viewWalletPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        builder
                .setContentTitle("Wallet created for ")
                .setContentText(wallet.getNickname())
                .setContentIntent(viewWalletPendingIntent)
                .setProgress(0, 0, false)
                .setVibrate(BeamItUp.SUCCESS_VIBRATE_PATTERN);

        notificationManagerCompat.notify(notificationID, builder.build());
    }
}
