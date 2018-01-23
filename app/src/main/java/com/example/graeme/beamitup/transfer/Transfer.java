package com.example.graeme.beamitup.transfer;


public class Transfer extends Transaction {
    private static final String TAG = "Transfer";

    protected Transfer(String toAddress, String amount) {
        super(toAddress, amount);
    }
}
