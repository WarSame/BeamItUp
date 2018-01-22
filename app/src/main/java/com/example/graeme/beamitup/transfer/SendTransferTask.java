package com.example.graeme.beamitup.transfer;

import android.os.AsyncTask;
import android.util.Log;

import com.example.graeme.beamitup.Session;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Convert;

import java.math.BigDecimal;

public class SendTransferTask extends AsyncTask<Transfer, Void, TransactionReceipt> {
    private static final String TAG = "SendTransferTask";
    private String receiverAddress;
    private Credentials credentials;
    private SendTransferResponse sendTransferResponse;

    public SendTransferTask(
            Credentials credentials,
            String receiverAddress,
            SendTransferResponse sendTransferResponse
    ){
        this.credentials = credentials;
        this.receiverAddress = receiverAddress;
        this.sendTransferResponse = sendTransferResponse;
    }

    public interface SendTransferResponse {
        void sendTransferFinish(TransactionReceipt transactionReceipt);
    }

    @Override
    protected TransactionReceipt doInBackground(Transfer... transfers) {
        Transfer transfer = transfers[0];
        try {
            Web3j web3j = Session.getWeb3j();

            Log.d(TAG, "Sender address: " + credentials.getAddress());

            TransactionReceipt receipt = org.web3j.tx.Transfer.sendFunds(
                    web3j,
                    credentials,
                    receiverAddress,
                    new BigDecimal(transfer.getAmount()),
                    Convert.Unit.ETHER
            ).send();

            Log.d(TAG, "Transfer from: " + receipt.getFrom());
            Log.d(TAG, "Transfer to: " + receipt.getTo());

            return receipt;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(TransactionReceipt res) {
        Log.i(TAG, "Transfer finished");
        sendTransferResponse.sendTransferFinish(res);
    }
}
