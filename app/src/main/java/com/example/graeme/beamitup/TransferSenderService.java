package com.example.graeme.beamitup;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Async;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class TransferSenderService extends Service {
    public static final String TAG = "TransferSenderService";
    private final IBinder binder = new TransferSenderBinder();

    class TransferSenderBinder extends Binder {
        TransferSenderService getService(){
            return TransferSenderService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent){
        return binder;
    }

    Future<TransactionReceipt> send(final Credentials credentials, final String receiverAddress) throws Exception {
        Log.i(TAG, "Credentials address: " + credentials.getAddress());
        Callable<TransactionReceipt> task = new Callable<TransactionReceipt>() {
            @Override
            public TransactionReceipt call() throws Exception {
                Web3j web3j = Web3jFactory.build(
                        new HttpService("https://rinkeby.infura.io/SxLC8uFzMPfzwnlXHqx9")
                );

                Log.d(TAG, "Sender address: " + credentials.getAddress());
                BigInteger balance = web3j.
                        ethGetBalance(credentials.getAddress(), DefaultBlockParameterName.LATEST).
                        sendAsync().get().getBalance();
                Log.d(TAG, "Sender balance: " + balance);

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
        };
        return Async.run(task);
    }
}
