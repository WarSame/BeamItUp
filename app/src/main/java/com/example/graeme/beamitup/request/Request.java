package com.example.graeme.beamitup.request;

import com.example.graeme.beamitup.transaction.Transaction;

public class Request extends Transaction {
    private static final String TAG = "Request";

    public Request(String toAddress, String amount) {
        super(toAddress, amount);
    }

    public Request() {
        super();
    }
}
