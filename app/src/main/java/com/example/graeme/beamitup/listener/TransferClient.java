package com.example.graeme.beamitup.listener;

import org.reactivestreams.Subscription;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.websocket.WebSocketListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransferClient {
    private List<String> addresses;
    private Web3j web3j;
    private TransferListener listener = null;

    public TransferClient(Web3j web3j){
        this.addresses = new ArrayList<>();
        this.web3j = web3j;
    }

    public void addAddress(String address){
        this.addresses.add(address);
    }

    public void setListener(TransferListener listener) {
        setPendingListener(listener);
        setTransactionListener(listener);
    }

    //Listen for pending transactions before they get added to a block
    private void setPendingListener(TransferListener listener) {
        web3j.transactionFlowable().subscribe(tx -> {
            if (addresses.contains( tx.getTo() ) ) {

            }
        }).dispose();
    }

    //Listen for transaction as they are added to a block
    private void setTransactionListener(TransferListener listener){
        this.listener = listener;
    }
}
