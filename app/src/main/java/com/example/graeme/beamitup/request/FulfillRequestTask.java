package com.example.graeme.beamitup.request;

import com.example.graeme.beamitup.transfer.SendTransactionTask;

import org.web3j.crypto.Credentials;

class FulfillRequestTask extends SendTransactionTask<Request> {
    FulfillRequestTask(Credentials credentials, String toAddress, SendTransferResponse sendTransferResponse) {
        super(credentials, toAddress, sendTransferResponse);
    }
}
