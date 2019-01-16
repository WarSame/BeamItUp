package com.example.graeme.beamitup.listener;

import android.content.Context;
import android.util.Log;

import com.example.graeme.beamitup.notifier.BlockNotifier;
import com.example.graeme.beamitup.notifier.PendingNotifier;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Transaction;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.reactivex.disposables.Disposable;

public class TransferClient {
    private static final String TAG = "TransferClient";
    private Set<String> addresses;
    private Web3j web3j;
    private Map<String, TransferState> notifications;
    private Context context;

    public TransferClient(
            Web3j web3j,
            Set<String> addresses
    ) {
        this.addresses = addresses;
        this.web3j = web3j;
        setPendingListener();
        setBlockListener();
        //this.addAddress("0x31B98D14007bDEe637298086988A0bBd31184523");//TODO:Remove after testing
        this.notifications = new HashMap<>();
    }

    public void addAddress(String address) {
        this.addresses.add(address.toLowerCase());
    }

    //Listen for pending transactions before they get added to a block
    private Disposable setPendingListener() {
        Log.i(TAG, "Adding pending listener");
        Disposable pendingDisposable = web3j.pendingTransactionFlowable().subscribe(this::onPendTx);
        Log.i(TAG, "Added pending listener");
        return pendingDisposable;
    }

    private void onPendTx(Transaction tx) {
        Log.d(TAG, "Received pending transaction to " + tx.getTo() + " from " + tx.getFrom()
                + " for amount " + tx.getValue());
        boolean isListenedAddress = addresses.contains(tx.getTo());
        if (isListenedAddress) {
            createPendingNotification(tx);
        }
    }

    private void createPendingNotification(Transaction tx) {
        Log.i(TAG, "Receiving pending transaction for address being listened to: " + tx.getTo());
        new PendingNotifier(context, tx, notifications).on_transaction();
    }

    //Listen for transaction as they are added to a block
    private Disposable setBlockListener() {
        Log.i(TAG, "Adding transaction listener");
        Disposable transactionDisposable = web3j.transactionFlowable().subscribe(this::onBlockTx);
        Log.i(TAG, "Added transaction listener");
        return transactionDisposable;
    }

    private void onBlockTx(Transaction tx) {
        Log.d(TAG, "Received transaction to " + tx.getTo() + " from " + tx.getFrom()
                + " for amount " + tx.getValue());
        boolean isListenedAddress = addresses.contains(tx.getTo());
        if (isListenedAddress) {
            createBlockNotification(tx);
        }
    }

    private void createBlockNotification(Transaction tx) {
        Log.i(TAG, "Receiving transaction for address being listened to: " + tx.getTo());
        new BlockNotifier(context, tx, notifications).on_transaction();
    }

}
