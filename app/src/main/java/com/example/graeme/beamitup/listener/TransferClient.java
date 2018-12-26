package com.example.graeme.beamitup.listener;

import android.util.Log;

import org.web3j.protocol.Web3j;

import java.util.HashSet;
import java.util.Set;

public class TransferClient {
    private static final String TAG = "TransferClient";
    private Set<String> addresses;
    private Web3j web3j;
    private TransferListener listener;

    public TransferClient(Web3j web3j, TransferListener listener){
        this.addresses = new HashSet<>();
        this.web3j = web3j;
        setListener(listener);
    }

    public void addAddress(String address){
        this.addresses.add(address.toLowerCase());
    }

    private void setListener(TransferListener listener) {
        this.listener = listener;
        setPendingListener();
        setTransactionListener();
    }

    //Listen for pending transactions before they get added to a block
    private void setPendingListener() {
        Log.d(TAG, "Added pending listener");
        web3j.pendingTransactionFlowable().subscribe(tx -> {
            Log.d(TAG, "To:" + tx.getTo());
            Log.d(TAG, "From: " + tx.getFrom());
            Log.d(TAG, "Amount: " + tx.getValue());
            if ( addresses.contains( tx.getTo() ) ){
                Log.d(TAG, "Received transaction to " + tx.getTo() + " from " + tx.getFrom()
                + " for amount " + tx.getValue());
            }
            listener.onMessage(tx.toString());
        });
    }

    //Listen for transaction as they are added to a block
    private void setTransactionListener(){
        Log.d(TAG, "Added transaction listener");
        web3j.transactionFlowable().subscribe(tx -> {
            //Log.d(TAG, "Hello");
            //listener.onMessage("someothermessage");
        });
    }
}
