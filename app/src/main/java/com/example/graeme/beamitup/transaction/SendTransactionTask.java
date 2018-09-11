package com.example.graeme.beamitup.transaction;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.graeme.beamitup.BeamItUp;
import com.example.graeme.beamitup.R;
import com.example.graeme.beamitup.transaction.SendTransactionService.OnSendTransaction;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.math.BigInteger;

public class SendTransactionTask extends AsyncTask<Transaction, Void, TransactionReceipt> {
    private WeakReference<Context> weakContext;
    private OnSendTransaction onSendTransaction;
    private static final String TAG = "SendTransactionTask";
    private Web3j web3j;

    SendTransactionTask(Context context, OnSendTransaction onSendTransaction){
        this.weakContext = new WeakReference<>(context);
        this.onSendTransaction = onSendTransaction;
        this.web3j = ((BeamItUp)weakContext.get().getApplicationContext()).getWeb3j();
    }

    @Override
    protected TransactionReceipt doInBackground(Transaction... transactions) {
        Transaction transaction = transactions[0];
        try {
            if (!isSufficientFunds(transaction.getFromCredentials().getAddress(), transaction.getAmount())){
                Log.i(TAG, "Insufficient funds");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Insufficient funds");
            return null;
        }

        int id = (int)System.currentTimeMillis();
        createNotification(transaction, id);

        Credentials credentials = transaction.getFromCredentials();
        Log.d(TAG, "Sender address: " + credentials.getAddress());

        TransactionReceipt receipt;
        try {
            receipt = org.web3j.tx.Transfer.sendFunds(
                    web3j,
                    credentials,
                    transaction.getToAddress(),
                    new BigDecimal(transaction.getAmount()),
                    Convert.Unit.ETHER
            ).send();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Failed to send transaction");
            return null;
        }
        handleTransactionSuccess(receipt, id);
        return receipt;
    }

    @Override
    protected void onPostExecute(TransactionReceipt receipt){
        onSendTransaction.onSendTransaction(receipt);
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
        NotificationCompat.Builder builder = new NotificationCompat.Builder(weakContext.get(), "BeamItUp")
                .setContentTitle("Creating wallet")
                .setContentText("Sending " + transaction.getAmount()
                        + " to " + transaction.getToAddress())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setProgress(0, 0, true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(weakContext.get());
        notificationManagerCompat.notify(id, builder.build());
    }

    private void handleTransactionSuccess(TransactionReceipt receipt, int notificationID){
        Log.d(TAG, "Transaction from: " + receipt.getFrom());
        Log.d(TAG, "Transaction to: " + receipt.getTo());

        Intent viewWalletIntent = new Intent(weakContext.get(), TransactionDetailActivity.class);
        viewWalletIntent.putExtra("receipt", new SerializableTransactionReceipt(receipt));

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(weakContext.get())
                .addNextIntentWithParentStack(viewWalletIntent);
        PendingIntent viewWalletPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(weakContext.get(), "BeamItUp")
                .setContentTitle("Transaction sent")
                .setContentText("Sent transaction from " + receipt.getFrom()
                        + " to " + receipt.getTo())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(viewWalletPendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(weakContext.get());
        notificationManagerCompat.notify(notificationID, builder.build());
    }
}
