package com.example.graeme.beamitup.listener;

import android.util.Log;

import org.web3j.protocol.Web3j;

import java.util.HashSet;
import java.util.Set;

import io.reactivex.disposables.Disposable;

public class TransferClient {
    private static final String TAG = "TransferClient";
    private Set<String> addresses;
    private Web3j web3j;
    private PendingListener pendingListener;
    private TransactionListener transactionListener;

    public TransferClient(
            Web3j web3j,
            PendingListener pendingListener,
            TransactionListener transactionListener
    ){
        this.addresses = new HashSet<>();
        this.web3j = web3j;
        setPendingListener(pendingListener);
        setTransactionListener(transactionListener);
    }

    public void addAddress(String address){
        this.addresses.add(address.toLowerCase());
    }

    //Listen for pending transactions before they get added to a block
    private Disposable setPendingListener(PendingListener pendingListener) {
        Log.d(TAG, "Adding pending listener");
        this.pendingListener = pendingListener;
        Disposable pendingDisposable = web3j.pendingTransactionFlowable().subscribe(tx -> {
            Log.d(TAG, "Received pending transaction to " + tx.getTo() + " from " + tx.getFrom()
                    + " for amount " + tx.getValue());
            if ( addresses.contains( tx.getTo() ) ){
                Log.d(TAG, "Receiving pending transaction for address being listened to: " + tx.getTo());
            }
            this.pendingListener.onMessage(tx.toString());
        });
        Log.d(TAG, "Added pending listener");
        return pendingDisposable;
    }

    //Listen for transaction as they are added to a block
    private Disposable setTransactionListener(TransactionListener transactionListener){
        Log.d(TAG, "Adding transaction listener");
        this.transactionListener = transactionListener;
        Disposable transactionDisposable = web3j.transactionFlowable().subscribe(tx -> {
            Log.d(TAG, "Received transaction to " + tx.getTo() + " from " + tx.getFrom()
                    + " for amount " + tx.getValue());
            if ( addresses.contains(tx.getTo() ) ){
                Log.d(TAG, "Receiving transaction for address being listened to: " + tx.getTo());
            }
            this.transactionListener.onMessage(tx.toString());
        });
        Log.d(TAG, "Added transaction listener");
        return transactionDisposable;
    }
}
