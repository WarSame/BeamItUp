package com.example.graeme.beamitup.request;

import com.example.graeme.beamitup.transfer.SendTransactionTask;

import org.web3j.crypto.Credentials;

class FulfillRequestTask extends SendTransactionTask<Request> {
    FulfillRequestTask(Credentials credentials, String receiverAddress, SendTransferResponse sendTransferResponse) {
        super(credentials, receiverAddress, sendTransferResponse);
    }
}
