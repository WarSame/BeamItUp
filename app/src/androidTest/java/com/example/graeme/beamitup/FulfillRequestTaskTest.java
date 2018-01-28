package com.example.graeme.beamitup;

import android.util.Log;

import com.example.graeme.beamitup.request.Request;
import com.example.graeme.beamitup.request.FulfillRequestTask;
import com.example.graeme.beamitup.transfer.SendTransactionTask.SendTransactionResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

import static org.junit.Assert.*;

public class FulfillRequestTaskTest {
    private static final String TAG = "SendTransferTaskTest";
    private static Request request;
    private static TransactionReceipt transactionReceipt;
    private static final String TRANSACTION_VALUE = "0.005";
    private static final String SENDER_ADDRESS = "0x6861b070f43842fc16ead07854ee5d91b9d27c13";
    private static final String RECEIVER_ADDRESS = "0x31b98d14007bdee637298086988a0bbd31184523";

    private static final String SENDER_PRIVATE_KEY = "";

    @BeforeClass
    public static void setUp() throws Exception {
        request = new Request();
        request.setFromAddress(SENDER_ADDRESS);
        request.setToAddress(RECEIVER_ADDRESS);
        request.setAmount(TRANSACTION_VALUE);

        Credentials credentials = Credentials.create(SENDER_PRIVATE_KEY);
        Session.createSession();//Empty session for testing

        FulfillRequestTask task = new FulfillRequestTask(
                Session.getWeb3j(),
                credentials,
                sendTransactionResponse
        );
        task.execute(request);
        transactionReceipt = task.get();
    }

    @After
    public void tearDown() throws Exception {
    }


    @Test
    public void checkSenderAddress() throws Exception{
        Log.i(TAG, "Sender address in request: " + request.getFromAddress());
        Log.i(TAG, "Sender address in TransactionReceipt: " + transactionReceipt.getFrom());
        assertTrue( transactionReceipt.getFrom().equals( request.getFromAddress() ) );
    }

    @Test
    public void checkReceiverAddress() throws Exception{
        Log.i(TAG, "Receiver address in request: " + request.getToAddress());
        Log.i(TAG, "Receiver address in TransactionReceipt: " + transactionReceipt.getTo());
        assertTrue( transactionReceipt.getTo().equals( request.getToAddress() ) );
    }

    @Test
    public void checkValue() throws Exception {
        Web3j web3j = Web3jFactory.build(
                new HttpService("https://rinkeby.infura.io/SxLC8uFzMPfzwnlXHqx9")
        );
        org.web3j.protocol.core.Request<?, EthTransaction> request = web3j.ethGetTransactionByHash(transactionReceipt.getTransactionHash());
        Log.i(TAG, "Transaction value: " + request.send().getTransaction().getValue());
    }

    private static SendTransactionResponse sendTransactionResponse = transactionReceipt -> {
        //Empty because we are running sync
    };

}