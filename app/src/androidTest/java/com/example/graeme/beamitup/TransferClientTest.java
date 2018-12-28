package com.example.graeme.beamitup;

import android.content.Context;
import android.util.Log;

import com.example.graeme.beamitup.listener.TransferClient;

import org.junit.BeforeClass;
import org.junit.Test;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.websocket.WebSocketService;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertTrue;
import static org.web3j.tx.Transfer.sendFunds;


public class TransferClientTest {
    private static final String TAG = "TransferClientTest";
    private static final String INFURA_URL = "wss://rinkeby.infura.io/ws";
    private static Context appContext;

    private static final String RECEIVING_ACCOUNT_ADDRESS = "0x31B98D14007bDEe637298086988A0bBd31184523";
    private static final String NON_RECEIVING_ACCOUNT_ADDRESS = "0x31B98D14007bDEe637297076988A0bBd31184523";
    private static Web3j web3j;

    @BeforeClass
    public static void setUpOneTime() throws Exception {
        WebSocketService webSocketService = new WebSocketService(INFURA_URL, false);
        webSocketService.connect();
        web3j = Web3j.build(webSocketService);
    }

    //Note: pending can come after transaction
    @Test
    public void listenForPendingTransfer_ShouldCountDown() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        TransferClient transferClient = new TransferClient(
                web3j,
                pending_tx -> {
                    Log.d(TAG, "Received pending transaction");
                    countDownLatch.countDown();
                },
                tx -> Log.d(TAG, "Received transaction")
        );
        transferClient.addAddress(RECEIVING_ACCOUNT_ADDRESS);
        sendFunds(
                web3j,
                FillWallet.retrieveMasterCredentials(),
                RECEIVING_ACCOUNT_ADDRESS,
                BigDecimal.ONE,
                Convert.Unit.MWEI
        ).send();
        assertTrue(countDownLatch.await(30, TimeUnit.SECONDS));
    }

    @Test
    public void listenForTransfer_ShouldCountDown() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        TransferClient transferClient = new TransferClient(
                web3j,
                pending_tx -> Log.d(TAG, "Received pending transaction"),
                tx -> {
                    Log.d(TAG, "Received transaction");
                    countDownLatch.countDown();
                }
        );
        transferClient.addAddress(RECEIVING_ACCOUNT_ADDRESS);
        sendFunds(
                web3j,
                FillWallet.retrieveMasterCredentials(),
                RECEIVING_ACCOUNT_ADDRESS,
                BigDecimal.ONE,
                Convert.Unit.MWEI
        ).send();
        assertTrue(countDownLatch.await(60, TimeUnit.SECONDS));
    }

    @Test
    public void listenForNotComingTransfer_ShouldNotBeTransfer()  {

    }
}
