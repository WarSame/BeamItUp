package com.example.graeme.beamitup.transfer;

import org.web3j.crypto.Credentials;

public class SendTransferTask extends SendTransactionTask<Transfer> {
    public SendTransferTask(Credentials credentials, String receiverAddress, SendTransactionResponse sendTransactionResponse) {
        super(credentials, receiverAddress, sendTransactionResponse);
    }
}
