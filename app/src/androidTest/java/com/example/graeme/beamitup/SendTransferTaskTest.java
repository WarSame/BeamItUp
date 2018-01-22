package com.example.graeme.beamitup;

import android.util.Log;

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

import static org.junit.Assert.*;

public class SendTransferTaskTest {
    private static final String TAG = "SendTransferTaskTest";
    private static Transfer transfer;
    private static TransactionReceipt transactionReceipt;
    private static final String TRANSACTION_VALUE = "55";
    private static final String SENDER_ADDRESS = "0x6861b070f43842fc16ead07854ee5d91b9d27c13";
    private static final String RECEIVER_ADDRESS = "0x31b98d14007bdee637298086988a0bbd31184523";

    @BeforeClass
    public static void oneTimeSetUp() throws Exception{
        transfer = new Transfer(TRANSACTION_VALUE, SENDER_ADDRESS);
        transfer.setReceiverAddress(RECEIVER_ADDRESS);

        Credentials credentials = Credentials.create("");

        SendTransferTask task = new SendTransferTask(credentials, transfer.getReceiverAddress(), sendTransferResponse);
        task.execute(transfer);
        transactionReceipt = task.get();
    }

    @Test
    public void checkSenderAddress() throws Exception{
        Log.i(TAG, "Sender address in transfer: " + transfer.getSenderAddress());
        Log.i(TAG, "Sender address in TransactionReceipt: " + transactionReceipt.getFrom());
        assertTrue( transactionReceipt.getFrom().equals( transfer.getSenderAddress() ) );
    }

    @Test
    public void checkReceiverAddress() throws Exception{
        Log.i(TAG, "Receiver address in transfer: " + transfer.getReceiverAddress());
        Log.i(TAG, "Receiver address in TransactionReceipt: " + transactionReceipt.getTo());
        assertTrue( transactionReceipt.getTo().equals( transfer.getReceiverAddress() ) );
    }

    @Test
    public void checkValue() throws Exception {
        Web3j web3j = Web3jFactory.build(
                new HttpService("https://rinkeby.infura.io/SxLC8uFzMPfzwnlXHqx9")
        );
        Request<?, EthTransaction> request = web3j.ethGetTransactionByHash(transactionReceipt.getTransactionHash());
        Log.i(TAG, "Transaction value: " + request.send().getTransaction().getValue());
    }

    private static SendTransferResponse sendTransferResponse = transactionReceipt -> {
        //Empty because we are running sync
    };

}