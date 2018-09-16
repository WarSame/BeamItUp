package com.example.graeme.beamitup.transaction;

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
import com.example.graeme.beamitup.request.Request;
import com.example.graeme.beamitup.transaction.SendTransactionService.OnSendTransaction;
import com.example.graeme.beamitup.wallet.Wallet;

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
    private NotificationCompat.Builder builder;
    private NotificationManagerCompat notificationManagerCompat;
    private int notificationID;

    SendTransactionTask(Context context, OnSendTransaction onSendTransaction){
        this.weakContext = new WeakReference<>(context);
        this.onSendTransaction = onSendTransaction;
        this.web3j = ((BeamItUp)weakContext.get().getApplicationContext()).getWeb3j();
        this.builder = new NotificationCompat.Builder(
                weakContext.get(),
                "BeamItUp"
        );
        this.builder.setSmallIcon(R.drawable.ic_beamitup);
        this.notificationManagerCompat = NotificationManagerCompat.from(weakContext.get());
        this.notificationID = (int)System.currentTimeMillis();
    }

    @Override
    protected TransactionReceipt doInBackground(Transaction... transactions) {
        Transaction transaction = transactions[0];
        Wallet senderWallet = transaction.getSenderWallet();
        Request request = transaction.getRequest();

        createNotification(transaction);

        Credentials credentials = null;
        try {
            credentials = senderWallet.retrieveCredentials();
            if (!isSufficientFunds(credentials.getAddress(), request.getAmount())){
                handleInsufficientFunds(transaction);
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Insufficient funds");

            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (credentials == null){
            Log.i(TAG, "Credentials are null");
            return null;
        }

        Log.d(TAG, "Sender address: " + credentials.getAddress());

        TransactionReceipt receipt;
        try {
            receipt = org.web3j.tx.Transfer.sendFunds(
                    web3j,
                    credentials,
                    request.getToAddress(),
                    new BigDecimal(request.getAmount()),
                    Convert.Unit.ETHER
            ).send();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Failed to send transaction");
            handleTransactionFailure(transaction);
            return null;
        }
        handleTransactionSuccess(receipt);
        return receipt;
    }

    private void handleInsufficientFunds(Transaction transaction) {
        Log.e(TAG, "Insufficient funds in account " + transaction.getSenderWallet().getNickname());
        builder.setContentTitle("Insufficient funds")
                .setContentText("Wallet " + transaction.getSenderWallet().getNickname()
                + " does not have " + transaction.getRequest().getAmount() + " ETH to send")
                .setProgress(0, 0, false);
        notificationManagerCompat.notify(notificationID, builder.build());
    }

    private void handleTransactionFailure(Transaction transaction) {
        builder.setContentTitle("Transaction failure")
                .setContentText("From " + transaction.getSenderWallet().getNickname()
                 + " to " + transaction.getRequest().getToAddress())
                .setProgress(0, 0, false);

        notificationManagerCompat.notify(notificationID, builder.build());
    }

    @Override
    protected void onPostExecute(TransactionReceipt receipt){
        onSendTransaction.onSendTransaction(receipt);
    }

    private boolean isSufficientFunds(String fromAddress, String amount) throws IOException {
        Log.i(TAG, "Checking if account " + fromAddress + " has " + amount + " ETH");
        BigInteger balance = web3j
                .ethGetBalance(fromAddress, DefaultBlockParameterName.LATEST)
                .send()
                .getBalance();

        Log.i(TAG, "BigInt balance = " + balance);

        BigDecimal balanceEth = Convert.fromWei(balance.toString(), Convert.Unit.ETHER);

        Log.i(TAG, "BigDec balanceEth = " + balanceEth);

        BigDecimal transactionAmount = new BigDecimal(amount);

        Log.i(TAG, "transactionAmount = " + transactionAmount);

        int comparator = balanceEth.compareTo(transactionAmount);

        Log.i(TAG, "comparator = " + comparator);

        boolean sufficientBalance = comparator >=0;

        Log.i(TAG, "sufficientBalance = " + sufficientBalance);

        return sufficientBalance;
    }

    private void createNotification(Transaction transaction){
        builder.setContentTitle("Sending transaction")
                .setContentText(transaction.getRequest().getAmount()
                        + " ETH to " + transaction.getRequest().getToAddress())
                .setProgress(0, 0, true);

        notificationManagerCompat.notify(notificationID, builder.build());
    }

    private void handleTransactionSuccess(TransactionReceipt receipt){
        Log.d(TAG, "Transaction from: " + receipt.getFrom());
        Log.d(TAG, "Transaction to: " + receipt.getTo());

        Intent viewWalletIntent = new Intent(weakContext.get(), TransactionDetailActivity.class);
        viewWalletIntent.putExtra("receipt", new SerializableTransactionReceipt(receipt));

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(weakContext.get())
                .addNextIntentWithParentStack(viewWalletIntent);
        PendingIntent viewWalletPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentTitle("Transaction success")
                .setContentText("Sent transaction from " + receipt.getFrom()
                        + " to " + receipt.getTo())
                .setContentIntent(viewWalletPendingIntent)
                .setProgress(0, 0, false);

        notificationManagerCompat.notify(notificationID, builder.build());
    }
}
