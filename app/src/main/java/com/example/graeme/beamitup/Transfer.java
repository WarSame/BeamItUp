package com.example.graeme.beamitup;

import android.util.Log;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Async;
import org.web3j.utils.Convert;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

class Transfer implements Serializable {
    private String amount;
    private String reason;
    private String senderAddress;
    private String receiverAddress;
    private static final String TAG = "Transfer";

    Transfer(String amount, String reason, String senderPublicKey){
        this.amount = amount;
        this.reason = reason;
        this.senderAddress = senderPublicKey;
        this.receiverAddress = null;
    }

    String getReason() {
        return reason;
    }

    String getAmount() {
        return amount;
    }

    public String toString (){
        return "Amount: " + amount + "\n"
                + "reason: " + reason + "\n"
                + "sender public key: " + senderAddress + "\n"
                + "receiver public key: " + receiverAddress;
    }

    String getReceiverAddress() {
        return receiverAddress;
    }

    void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    Future<TransactionReceipt> send(final Credentials credentials) throws Exception {
        Log.i(TAG, "Credentials address: " + credentials.getAddress());
        Callable<TransactionReceipt> task = new Callable<TransactionReceipt>() {
            @Override
            public TransactionReceipt call() throws Exception {
                Web3j web3 = Web3jFactory.build(
                        new HttpService("https://rinkeby.infura.io/SxLC8uFzMPfzwnlXHqx9")
                );
                Log.d(TAG, "Client version: " + web3.web3ClientVersion().send().getWeb3ClientVersion());

                Log.d(TAG, "Credentials address: " + credentials.getAddress());
                BigInteger balance = web3.ethGetBalance(credentials.getAddress(), DefaultBlockParameterName.LATEST).
                        sendAsync().get().getBalance();
                Log.d(TAG, "Address balance: " + balance);

                TransactionReceipt receipt = org.web3j.tx.Transfer.sendFunds(
                        web3,
                        credentials,
                        getReceiverAddress(),
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

    String getSenderAddress() {
        return senderAddress;
    }

    void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }
}
