package com.example.graeme.beamitup.transaction;

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
import com.example.graeme.beamitup.wallet.WalletDetailActivity;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class SendTransactionService extends Service {
    private static final String TAG = "SendTransactionService";
    private Web3j web3j;
    private final IBinder binder = new SendTransactionBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        web3j = ((BeamItUp)getApplication()).getWeb3j();
    }

    public interface OnSendTransaction{
        void onSendTransaction(TransactionReceipt receipt);
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
        Log.i(TAG, "Sending transaction");
        new SendTransactionTask(getApplicationContext(), onSendTransaction).execute(transaction);
    }


    public class InsufficientFundsException extends Throwable {
        private static final long serialVersionUID = -7404820607502238067L;
    }
}
