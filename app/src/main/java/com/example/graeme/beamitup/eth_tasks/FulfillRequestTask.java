package com.example.graeme.beamitup.eth_tasks;

import com.example.graeme.beamitup.eth_tasks.SendTransactionTask;
import com.example.graeme.beamitup.request.Request;

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
