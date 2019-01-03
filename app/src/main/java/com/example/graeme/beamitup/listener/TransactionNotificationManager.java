package com.example.graeme.beamitup.listener;

import android.content.Context;

import com.example.graeme.beamitup.wallet.Wallet;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Transaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionNotificationManager {
    private Map<String, TransactionNotification> notifications;
    private TransferClient transferClient;
    private Context context;

    public TransactionNotificationManager(
            List<Wallet> wallets,
            Web3j web3j,
            Context context
    ){
        this.notifications = new HashMap<>();
        this.context = context;
        for (Wallet wallet: wallets) {
            transferClient.addAddress(wallet.getAddress());
        }
        transferClient = new TransferClient(
                web3j,
                this::onPendingTx,
                this::onBlockTx
        );
    }

    private void onPendingTx(Transaction pend_tx){
        if ( this.notifications.containsKey( pend_tx.getHash() ) ){
            this.notifications.get( pend_tx.getHash() );
            return;
        }
        TransactionNotification transactionNotification = new TransactionNotification(
                context,
                pend_tx
        );
        this.notifications.put(pend_tx.getHash(), transactionNotification);
    }

    private void onBlockTx(Transaction tx){
        new TransactionNotification(
                context,
                tx
        );
    }
}
