package com.example.graeme.beamitup.transaction;

import com.example.graeme.beamitup.request.Request;
import com.example.graeme.beamitup.wallet.Wallet;

import java.io.Serializable;

public class Transaction {
    public static final String TAG = "Transaction";
    private static final long serialVersionUID = 9124947053743920362L;
    private Wallet senderWallet;
    private Request request;

    public Transaction(Wallet senderWallet, Request request){
        this.senderWallet = senderWallet;
        this.request = request;
    }

    public Wallet getSenderWallet() {
        return senderWallet;
    }

    public Request getRequest() {
        return request;
    }
}
