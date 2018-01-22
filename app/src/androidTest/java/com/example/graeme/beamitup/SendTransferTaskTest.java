package com.example.graeme.beamitup;

import android.util.Log;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

import com.example.graeme.beamitup.SendTransferTask.SendTransferResponse;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

public class SendTransferTaskTest {
    private static final String TAG = "SendTransferTaskTest";
    private static Transfer transfer;
    private static TransactionReceipt transactionReceipt;

    @BeforeClass
    public static void oneTimeSetUp() throws Exception{
        transfer = new Transfer("55", "0x6861b070f43842fc16ead07854ee5d91b9d27c13");
        transfer.setReceiverAddress("0x31b98d14007bdee637298086988a0bbd31184523");

        Credentials credentials = Credentials.create("");

        SendTransferTask task = new SendTransferTask(credentials, transfer.getReceiverAddress(), sendTransferResponse);
        task.execute(transfer);
        transactionReceipt = task.get();
    }

    @Test
    public void doInBackground() throws Exception {
        Log.i(TAG, "Sender address: " + transactionReceipt.getFrom());
        Log.i(TAG, "Receiver address: " + transactionReceipt.getTo());
        assertTrue( transactionReceipt.getFrom().equals( transfer.getSenderAddress() ) );
        assertTrue( transactionReceipt.getTo().equals( transfer.getReceiverAddress() ) );

        Web3j web3j = Web3jFactory.build(
                new HttpService("https://rinkeby.infura.io/SxLC8uFzMPfzwnlXHqx9")
        );
        Request<?, EthTransaction> request = web3j.ethGetTransactionByHash(transactionReceipt.getTransactionHash());
        Log.i(TAG, "Transaction value: " + request.send().getTransaction().getValue());
    }

    private static SendTransferResponse sendTransferResponse = new SendTransferResponse() {
        @Override
        public void sendTransferFinish(TransactionReceipt transactionReceipt) {
            //Empty because we are running sync
        }
    };

}