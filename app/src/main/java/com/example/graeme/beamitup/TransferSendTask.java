package com.example.graeme.beamitup;

import android.os.AsyncTask;
import android.util.Log;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;

public class TransferSendTask extends AsyncTask<Transfer, Void, TransactionReceipt> {
    private static final String TAG = "TransferSendTask";

    @Override
    protected TransactionReceipt doInBackground(Transfer... transfer) {
        final Credentials credentials = Credentials.create("");
        final String receiverAddress = "0x31B98D14007bDEe637298086988A0bBd31184523".toLowerCase();

        try {
            Web3j web3j = Web3jFactory.build(
                    new HttpService("https://rinkeby.infura.io/SxLC8uFzMPfzwnlXHqx9")
            );

            Log.d(TAG, "Sender address: " + credentials.getAddress());

            TransactionReceipt receipt = org.web3j.tx.Transfer.sendFunds(
                    web3j,
                    credentials,
                    receiverAddress,
                    BigDecimal.ONE,
                    Convert.Unit.WEI
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
    }
}
