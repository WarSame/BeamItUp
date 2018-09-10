package com.example.graeme.beamitup.wallet;

import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.AsyncTask;
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

    public interface OnGenerateWallet{
        void onGenerateWallet(Wallet wallet);
    }

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

    public void generateWallet(String nickname, OnGenerateWallet onGenerateWallet){
        Log.i(TAG, "Generating wallet for " + nickname);
        new GenerateWalletTask(this, isUserAuthenticationRequired, onGenerateWallet).execute(nickname);
    }
}
