package com.example.graeme.beamitup.transaction;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.web3j.protocol.core.methods.response.TransactionReceipt;

public class SendTransactionService extends Service {
    private static final String TAG = "SendTransactionService";
    private final IBinder binder = new SendTransactionBinder();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public interface OnSendTransaction{
        void onSendTransaction(Transaction transaction);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class SendTransactionBinder extends Binder {
        public SendTransactionService getService(){
            return SendTransactionService.this;
        }
    }


    public void sendTransaction(Transaction transaction, OnSendTransaction onSendTransaction)
    {
        Log.i(TAG, "Sending transaction from " + transaction.getSenderWallet().getNickname());
        Log.i(TAG, "Sending transaction from " + transaction.getSenderWallet().getAddress());
        new SendTransactionTask(getApplicationContext(), onSendTransaction).execute(transaction);
    }
}
