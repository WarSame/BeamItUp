package com.example.graeme.beamitup.request;

import com.example.graeme.beamitup.transfer.SendTransactionTask;

import org.web3j.crypto.Credentials;

public class FulfillRequestTask extends SendTransactionTask<Request> {
    public FulfillRequestTask(Credentials credentials, String toAddress, SendTransactionResponse sendTransactionResponse) {
        super(credentials, toAddress, sendTransactionResponse);
    }
}
