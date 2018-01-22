package com.example.graeme.beamitup;

import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import com.example.graeme.beamitup.SendTransferTask.SendTransferResponse;

import static org.junit.Assert.*;

public class SendTransferTaskTest {
    private static final String TAG = "SendTransferTaskTest";
    private Transfer transfer;

    @Before
    public void setUp() throws Exception {
        //Lower case to avoid mismatch with transaction receipt values
        transfer = new Transfer("55", "Because", "0x6861B070f43842FC16eAD07854eE5D91B9D27C13".toLowerCase());
        transfer.setReceiverAddress("0x31B98D14007bDEe637298086988A0bBd31184523".toLowerCase());
    }

    @Test
    public void doInBackground() throws Exception {
        Credentials credentials = Credentials.create("");

        SendTransferResponse sendTransferResponse = new SendTransferResponse() {
            @Override
            public void sendTransferFinish(TransactionReceipt transactionReceipt) {
            }
        };
        SendTransferTask task = new SendTransferTask(credentials, transfer.getReceiverAddress(), sendTransferResponse);
        task.execute(transfer);
        TransactionReceipt transactionReceipt = task.get();
        Log.i(TAG, "Sender address: " + transactionReceipt.getFrom());
        Log.i(TAG, "Receiver address: " + transactionReceipt.getTo());
        assertTrue( transactionReceipt.getFrom().equals( transfer.getSenderAddress() ) );
        assertTrue( transactionReceipt.getTo().equals( transfer.getReceiverAddress() ) );
    }

}