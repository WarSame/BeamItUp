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


    public TransactionReceipt sendTransaction(Transaction transaction)
            throws Exception, InsufficientFundsException
    {
        if (!isSufficientFunds(transaction.getFromCredentials().getAddress(), transaction.getAmount())){
            throw new InsufficientFundsException();
        }

        int id = (int)System.currentTimeMillis();
        createNotification(transaction, id);

        Credentials credentials = transaction.getFromCredentials();
        Log.d(TAG, "Sender address: " + credentials.getAddress());

        TransactionReceipt receipt = org.web3j.tx.Transfer.sendFunds(
                web3j,
                credentials,
                transaction.getToAddress(),
                new BigDecimal(transaction.getAmount()),
                Convert.Unit.ETHER
        ).send();

        handleTransactionSuccess(transaction, id);
        return receipt;
    }

    private boolean isSufficientFunds(String fromAddress, String amount) throws IOException {
        BigInteger balance = web3j
                .ethGetBalance(fromAddress, DefaultBlockParameterName.LATEST)
                .send()
                .getBalance();

        BigDecimal balanceDec = new BigDecimal(balance);

        BigDecimal transactionAmount = new BigDecimal(amount);
        return balanceDec.compareTo(transactionAmount) >= 0;
    }

    private void createNotification(Transaction transaction, int id){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "BeamItUp")
                .setContentTitle("Creating wallet")
                .setContentText("Sending " + transaction.getAmount()
                        + " to " + transaction.getToAddress())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setProgress(0, 0, true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(id, builder.build());
    }

    private void handleTransactionSuccess(Transaction transaction, int notificationID){
        Log.d(TAG, "Transaction from: " + transaction.getFromCredentials().getAddress());
        Log.d(TAG, "Transaction to: " + transaction.getToAddress());

        Intent viewWalletIntent = new Intent(this, TransactionDetailActivity.class);
        viewWalletIntent.putExtra("transaction", transaction);

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
        notificationManagerCompat.notify(notificationID, builder.build());
    }

    public class InsufficientFundsException extends Throwable {
        private static final long serialVersionUID = -7404820607502238067L;


    }
}
