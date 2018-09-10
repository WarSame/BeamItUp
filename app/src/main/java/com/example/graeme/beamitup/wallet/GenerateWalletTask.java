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

    GenerateWalletTask(Context context, boolean isUserAuthenticationRequired, OnGenerateWallet onGenerateWallet){
        this.weakContext = new WeakReference<>(context);
        this.isUserAuthenticationRequired = isUserAuthenticationRequired;
        this.onGenerateWallet = onGenerateWallet;
    }

    @Override
    protected Wallet doInBackground(String... nicknames) {
        int id = (int)System.currentTimeMillis();

        String nickname = nicknames[0];
        Log.d(TAG, "Generating wallet for " + nickname);
        builder = new NotificationCompat.Builder(weakContext.get(), "BeamItUp")
                .setContentTitle("Creating wallet")
                .setContentText(nickname)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setProgress(0, 0, true);

        notificationManagerCompat = NotificationManagerCompat.from(weakContext.get());
        notificationManagerCompat.notify(id, builder.build());

        Wallet wallet = null;
        try {
            wallet = handleWalletCreation(nickname, id);
        } catch (Exception e){
            e.printStackTrace();
        }
        return wallet;
    }

    @Override
    protected void onPostExecute(Wallet wallet){
        onGenerateWallet.onGenerateWallet(wallet);
    }

    private Wallet handleWalletCreation(String nickname, int notificationID) throws Exception{
        Wallet wallet = new Wallet.WalletBuilder()
                .nickname(nickname)
                .context(weakContext.get())
                .isUserAuthenticationRequired(isUserAuthenticationRequired)
                .build();

        insertWallet(wallet);

        onCreateWalletSuccess(wallet, notificationID);
        return wallet;
    }

    private void insertWallet(Wallet wallet){
        Log.i(TAG, "Inserting new wallet");
        DaoSession daoSession = ((BeamItUp)weakContext.get().getApplicationContext()).getDaoSession();
        WalletDao walletDao = daoSession.getWalletDao();
        walletDao.insert(wallet);
        Log.i(TAG, "Inserted new wallet " + wallet.getId());
    }

    private void onCreateWalletSuccess(Wallet wallet, int notificationID){
        Log.i(TAG, "Wallet created");
        Intent viewWalletIntent = new Intent(weakContext.get(), WalletDetailActivity.class);
        viewWalletIntent.putExtra("wallet", wallet);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(weakContext.get())
                .addNextIntentWithParentStack(viewWalletIntent);

        PendingIntent viewWalletPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        builder
                .setContentText("Wallet created for " + wallet.getNickname())
                .setContentIntent(viewWalletPendingIntent);
        notificationManagerCompat.notify(notificationID, builder.build());
    }
}
