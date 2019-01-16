package com.example.graeme.beamitup.listener;

import android.util.Log;

import com.example.graeme.beamitup.wallet.Wallet;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Transaction;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.disposables.Disposable;

public class TransferClient {
    private static final String TAG = "TransferClient";
    private Set<String> addresses;
    private Web3j web3j;
    private Map<String, TransactionNotification> notifications;

    public TransferClient(
            Web3j web3j,
            Set<String> addresses,
            PendingListener pendingListener,
            BlockListener blockListener
    ) {
        this.addresses = addresses;
        this.web3j = web3j;
        setPendingListener(pendingListener);
        setBlockListener(blockListener);
        this.addAddress("0x31B98D14007bDEe637298086988A0bBd31184523");//TODO:Remove after testing
        this.notifications = new HashMap<>();
    }

    public void addAddress(String address) {
        this.addresses.add(address.toLowerCase());
    }

    //Listen for pending transactions before they get added to a block
    private Disposable setPendingListener(PendingListener pendingListener) {
        Log.i(TAG, "Adding pending listener");
        Disposable pendingDisposable = web3j.pendingTransactionFlowable().subscribe(this::onPendTx);
        Log.i(TAG, "Added pending listener");
        return pendingDisposable;
    }

    private void onPendTx(Transaction tx) {
        Log.d(TAG, "Received pending transaction to " + tx.getTo() + " from " + tx.getFrom()
                + " for amount " + tx.getValue());
        if (addresses.contains(tx.getTo())) {
            Log.i(TAG, "Receiving pending transaction for address being listened to: " + tx.getTo());
            notifications.get(tx.getHash())
                    .notify_pend_tx(notifications);
        }
    }

    //Listen for transaction as they are added to a block
    private Disposable setBlockListener(BlockListener blockListener) {
        Log.i(TAG, "Adding transaction listener");
        Disposable transactionDisposable = web3j.transactionFlowable().subscribe(this::onBlockTx);
        Log.i(TAG, "Added transaction listener");
        return transactionDisposable;
    }

    private void onBlockTx(Transaction tx) {
        Log.d(TAG, "Received transaction to " + tx.getTo() + " from " + tx.getFrom()
                + " for amount " + tx.getValue());
        if (addresses.contains(tx.getTo())) {
            Log.i(TAG, "Receiving transaction for address being listened to: " + tx.getTo());
            notifications.get(tx.getHash())
                    .notify_block_tx(notifications);
        }
    }

    public static class TransferClientBuilder {
        private Web3j web3j;
        private Set<String> addresses;
        private PendingListener pendingListener;
        private BlockListener blockListener;
        public TransferClientBuilder(){
        }

        public TransferClientBuilder web3j(Web3j web3j){
            this.web3j = web3j;
            return this;
        }

        public TransferClientBuilder addresses(Set<String> addresses){
            this.addresses = addresses;
            return this;
        }

        public TransferClientBuilder pendingListener(PendingListener pendingListener){
            this.pendingListener = pendingListener;
            return this;
        }

        public TransferClientBuilder blockListener(BlockListener blockListener){
            this.blockListener = blockListener;
            return this;
        }

        public TransferClient build(){
            return new TransferClient(
                    this.web3j,
                    this.addresses,
                    this.pendingListener,
                    this.blockListener
            );
        }
    }
}
