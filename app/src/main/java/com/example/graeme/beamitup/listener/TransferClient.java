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
        this.addAddress("0x31B98D14007bDEe637298086988A0bBd31184523");//TODO:Remove after testing
    }

    public void addAddress(String address){
        this.addresses.add(address.toLowerCase());
    }

    //Listen for pending transactions before they get added to a block
    private Disposable setPendingListener(PendingListener pendingListener) {
        Log.i(TAG, "Adding pending listener");
        this.pendingListener = pendingListener;
        Disposable pendingDisposable = web3j.pendingTransactionFlowable().subscribe(tx -> {
            Log.d(TAG, "Received pending transaction to " + tx.getTo() + " from " + tx.getFrom()
                    + " for amount " + tx.getValue());
            if ( addresses.contains( tx.getTo() ) ){
                Log.i(TAG, "Receiving pending transaction for address being listened to: " + tx.getTo());
                this.pendingListener.onMessage(tx);
            }
        });
        Log.i(TAG, "Added pending listener");
        return pendingDisposable;
    }

    //Listen for transaction as they are added to a block
    private Disposable setTransactionListener(TransactionListener transactionListener){
        Log.i(TAG, "Adding transaction listener");
        this.transactionListener = transactionListener;
        Disposable transactionDisposable = web3j.transactionFlowable().subscribe(tx -> {
            Log.d(TAG, "Received transaction to " + tx.getTo() + " from " + tx.getFrom()
                    + " for amount " + tx.getValue());
            if ( addresses.contains(tx.getTo() ) ){
                Log.i(TAG, "Receiving transaction for address being listened to: " + tx.getTo());
                this.transactionListener.onMessage(tx);
            }
        });
        Log.i(TAG, "Added transaction listener");
        return transactionDisposable;
    }
}
