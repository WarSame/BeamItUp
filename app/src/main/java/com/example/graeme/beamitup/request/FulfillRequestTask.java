package com.example.graeme.beamitup.request;

import com.example.graeme.beamitup.transfer.SendTransactionTask;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;

public class FulfillRequestTask extends SendTransactionTask<Request> {
    public FulfillRequestTask(
            Web3j web3j,
            Credentials credentials,
            SendTransactionResponse sendTransactionResponse
    ) {
        super(web3j, credentials, sendTransactionResponse);
    }
}
