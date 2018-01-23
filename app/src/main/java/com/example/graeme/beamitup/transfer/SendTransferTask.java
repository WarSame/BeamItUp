package com.example.graeme.beamitup.transfer;

import android.os.AsyncTask;
import android.util.Log;

import com.example.graeme.beamitup.Session;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Convert;

import java.math.BigDecimal;

public class SendTransferTask extends SendTransactionTask<Transfer> {
    public SendTransferTask(Credentials credentials, String receiverAddress, SendTransferResponse sendTransferResponse) {
        super(credentials, receiverAddress, sendTransferResponse);
    }
}
