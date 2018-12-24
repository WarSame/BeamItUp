package com.example.graeme.beamitup.listener;

import android.util.Log;

import org.web3j.protocol.Web3j;
import java.util.ArrayList;
import java.util.List;

public class TransferClient {
    private static final String TAG = "TransferClient";
    private List<String> addresses;
    private Web3j web3j;
    private TransferListener listener;

    public TransferClient(Web3j web3j, TransferListener listener){
        this.addresses = new ArrayList<>();
        this.web3j = web3j;
        this.listener = listener;
    }

    public void addAddress(String address){
        this.addresses.add(address);
    }

    public void setListener(TransferListener listener) {
        setPendingListener();
        setTransactionListener();
    }

    //Listen for pending transactions before they get added to a block
    private void setPendingListener() {
        web3j.pendingTransactionFlowable().subscribe(tx -> {
            if (addresses.contains( tx.getTo() ) ) {
                Log.d(TAG, "Received transfer to " + tx.getTo() + "  from " + tx.getFrom() + " " +
                        "for amount " + tx.getValue());
                listener.onMessage("somemessage");
            }
        }).dispose();
    }

    //Listen for transaction as they are added to a block
    private void setTransactionListener(){
        web3j.transactionFlowable().subscribe(tx -> {
            if (addresses.contains( tx.getTo() ) ) {
                Log.d(TAG, "Hello");
                listener.onMessage("someothermessage");
            }
        }).dispose();
    }
}
