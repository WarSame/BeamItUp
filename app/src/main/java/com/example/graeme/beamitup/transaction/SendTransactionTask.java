package com.example.graeme.beamitup.transaction;

import android.os.AsyncTask;
import android.util.Log;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Convert;

import java.math.BigDecimal;

public class SendTransactionTask extends AsyncTask<Transaction, Void, TransactionReceipt> {
    private static final String TAG = "SendTransactionTask";
    private Web3j web3j;

    public SendTransactionTask(Web3j web3j){
        this.web3j = web3j;
    }

    @Override
    protected TransactionReceipt doInBackground(Transaction... transactions) {
        Transaction transaction = transactions[0];
        TransactionReceipt transactionReceipt = null;
        try {
            transactionReceipt = sendTransaction(transaction);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactionReceipt;
    }

    private TransactionReceipt sendTransaction(Transaction transaction)
            throws Exception
    {
        Credentials credentials = transaction.getFromCredentials();
        Log.d(TAG, "Sender address: " + credentials.getAddress());

        Web3j web3j = this.web3j;
        return org.web3j.tx.Transfer.sendFunds(
                web3j,
                credentials,
                transaction.getToAddress(),
                new BigDecimal(transaction.getAmount()),
                Convert.Unit.ETHER
        ).send();
    }
}
