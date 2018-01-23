package com.example.graeme.beamitup.request;

import com.example.graeme.beamitup.transfer.Transaction;

class Request extends Transaction {
    private static final String TAG = "Request";

    Request(String toAddress, String amount) {
        super(toAddress, amount);
    }
}
