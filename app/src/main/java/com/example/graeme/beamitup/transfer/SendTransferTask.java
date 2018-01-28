package com.example.graeme.beamitup.transfer;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;

public class SendTransferTask extends SendTransactionTask<Transfer> {
    public SendTransferTask(Web3j web3j, Credentials credentials, SendTransactionResponse sendTransactionResponse) {
        super(web3j, credentials, sendTransactionResponse);
    }
}
