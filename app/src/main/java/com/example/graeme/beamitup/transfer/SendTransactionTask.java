package com.example.graeme.beamitup.transfer;

import android.os.AsyncTask;
import android.util.Log;

import com.example.graeme.beamitup.Session;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Convert;

import java.math.BigDecimal;

public class SendTransactionTask<T extends Transaction> extends AsyncTask<T, Void, TransactionReceipt> {
    private static final String TAG = "SendTransactionTask";
    private String toAddress;
    private Web3j web3j;
    private Credentials credentials;
    private SendTransactionResponse sendTransactionResponse;

    public SendTransactionTask(
            Web3j web3j,
            Credentials credentials,
            String toAddress,
            SendTransactionResponse sendTransactionResponse
    ){
        this.web3j = web3j;
        this.credentials = credentials;
        this.toAddress = toAddress;
        this.sendTransactionResponse = sendTransactionResponse;
    }

    public interface SendTransactionResponse {
        void sendTransactionFinish(TransactionReceipt transactionReceipt);
    }

    @SafeVarargs
    @Override
    protected final TransactionReceipt doInBackground(T... transactions) {
        Transaction transaction = transactions[0];
        try {
            Log.d(TAG, "Sender address: " + credentials.getAddress());
            TransactionReceipt receipt = org.web3j.tx.Transfer.sendFunds(
                    web3j,
                    credentials,
                    toAddress,
                    new BigDecimal(transaction.getAmount()),
                    Convert.Unit.ETHER
            ).send();

            Log.d(TAG, "Transaction from: " + receipt.getFrom());
            Log.d(TAG, "Transaction to: " + receipt.getTo());

            return receipt;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(TransactionReceipt res) {
        Log.i(TAG, "Transaction finished");
        sendTransactionResponse.sendTransactionFinish(res);
    }
}
