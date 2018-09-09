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
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Convert;
import java.math.BigDecimal;

public class SendTransactionService extends Service {
    private static final String TAG = "SendTransactionService";
    private Web3j web3j;
    private final IBinder binder = new SendTransactionBinder();

    public SendTransactionService(){
    }

    @Override
    public void onCreate() {
        super.onCreate();
        web3j = ((BeamItUp)getApplication()).getWeb3j();
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

    private int id = (int)System.currentTimeMillis();

    public TransactionReceipt sendTransaction(Transaction transaction){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "BeamItUp")
                .setContentTitle("Creating wallet")
                .setContentText("Sending " + transaction.getAmount()
                        + " to " + transaction.getToAddress())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setProgress(0, 0, true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(id, builder.build());

        TransactionReceipt receipt = null;
        try {
            Credentials credentials = transaction.getFromCredentials();
            Log.d(TAG, "Sender address: " + credentials.getAddress());

            receipt = org.web3j.tx.Transfer.sendFunds(
                    web3j,
                    credentials,
                    transaction.getToAddress(),
                    new BigDecimal(transaction.getAmount()),
                    Convert.Unit.ETHER
            ).send();

            handleTransactionSuccess(transaction);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return receipt;
    }

    private void handleTransactionSuccess(Transaction transaction){
        Log.d(TAG, "Transaction from: " + transaction.getFromCredentials().getAddress());
        Log.d(TAG, "Transaction to: " + transaction.getToAddress());

        Intent viewWalletIntent = new Intent(this, TransactionDetailActivity.class);
        viewWalletIntent.putExtra("receipt", transaction);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this)
                .addNextIntentWithParentStack(viewWalletIntent);
        PendingIntent viewWalletPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "BeamItUp")
                .setContentTitle("Transaction sent")
                .setContentText("Sent " + transaction.getAmount()
                + " to " + transaction.getToAddress())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(viewWalletPendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(id, builder.build());
    }
}
