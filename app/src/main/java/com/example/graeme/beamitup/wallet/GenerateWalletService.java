package com.example.graeme.beamitup.wallet;

import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.graeme.beamitup.BeamItUp;
import com.example.graeme.beamitup.R;

public class GenerateWalletService extends Service {
    private static final String TAG = "GenerateWalletService";
    private IBinder binder = new GenerateWalletBinder();
    private boolean isUserAuthenticationRequired = true;

    public class GenerateWalletBinder extends Binder {
        public GenerateWalletService getService(){
            return GenerateWalletService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void setIsUserAuthenticationRequired(boolean isUserAuthenticationRequired){
        this.isUserAuthenticationRequired = isUserAuthenticationRequired;
    }

    public Wallet generateWallet(String nickname) throws Exception {
        int id = (int)System.currentTimeMillis();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "BeamItUp")
                .setContentTitle("Creating wallet")
                .setContentText(nickname)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setProgress(0, 0, true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(id, builder.build());

        return handleWalletCreation(nickname, id);
    }

    private Wallet handleWalletCreation(String nickname, int notificationID) throws Exception{
        Wallet wallet = new Wallet.WalletBuilder()
            .nickname(nickname)
            .context(this)
            .isUserAuthenticationRequired(isUserAuthenticationRequired)
            .build();

        insertWallet(wallet);

        onCreateWalletSuccess(wallet, notificationID);
        return wallet;
    }

    private void insertWallet(Wallet wallet){
        Log.i(TAG, "Inserting new wallet");
        DaoSession daoSession = ((BeamItUp)getApplication()).getDaoSession();
        WalletDao walletDao = daoSession.getWalletDao();
        walletDao.insert(wallet);
        Log.i(TAG, "Inserted new wallet " + wallet.getId());
    }

    private void onCreateWalletSuccess(Wallet wallet, int notificationID){
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
        notificationManagerCompat.notify(notificationID, builder.build());
    }
}
