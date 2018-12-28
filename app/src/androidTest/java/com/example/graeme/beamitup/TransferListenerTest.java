package com.example.graeme.beamitup;

import android.util.Log;

import org.junit.BeforeClass;
import org.junit.Test;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.websocket.WebSocketService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;

public class TransferListenerTest {
    private static final String TAG = "TransferListenerTest";
    private static final String INFURA_URL = "wss://rinkeby.infura.io/ws";
    private static Web3j web3j;

    @BeforeClass
    public static void setUpOneTime() throws Exception {
        WebSocketService webSocketService = new WebSocketService(INFURA_URL, false);
        webSocketService.connect();
        web3j = Web3j.build(webSocketService);
    }

    @Test
    public void listenForPendingTransactions_ShouldBeTransactions() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        List<Transaction> transactions = new ArrayList<>();

        Disposable sub = web3j.pendingTransactionFlowable().subscribe(tx -> {
            Log.d(TAG, "Some message");
            Log.d(TAG, tx.toString());
            transactions.add(tx);
            countDownLatch.countDown();
        });

        countDownLatch.await(20, TimeUnit.SECONDS);
        sub.dispose();

        if (transactions.size() == 0){
            throw new Exception("No transactions found");
        }
    }
}
