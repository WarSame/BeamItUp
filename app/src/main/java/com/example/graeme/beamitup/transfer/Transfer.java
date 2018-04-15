package com.example.graeme.beamitup.transfer;


import com.example.graeme.beamitup.transaction.Transaction;

public class Transfer extends Transaction {
    private static final String TAG = "Transfer";

    public Transfer(String toAddress, String amount) {
        super(toAddress, amount);
    }

    public Transfer() {
    }
}
