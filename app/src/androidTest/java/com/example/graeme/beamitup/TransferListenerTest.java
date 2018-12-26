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

import static org.web3j.tx.Transfer.sendFunds;


public class TransferListenerTest {
    private static final String TAG = "TransferListenerTest";
    private static final String INFURA_URL = "wss://rinkeby.infura.io/ws";
    private static Context appContext;

    private static final String RECEIVING_ACCOUNT_ADDRESS = "0x31B98D14007bDEe637298086988A0bBd31184523";
    private static final String NON_RECEIVING_ACCOUNT_ADDRESS = "";
    private static Web3j web3j;

    @BeforeClass
    public static void setUpOneTime() throws Exception {
        WebSocketService webSocketService = new WebSocketService(INFURA_URL, false);
        webSocketService.connect();
        web3j = Web3j.build(webSocketService);
    }

    @Test
    public void listenForTransfer_ShouldBeTransfer() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        TransferClient transferClient = new TransferClient(web3j, message -> {
            Log.d(TAG, "Somethirdmessage");
            countDownLatch.countDown();
        });
        transferClient.addAddress(RECEIVING_ACCOUNT_ADDRESS);
        sendFunds(
                web3j,
                FillWallet.retrieveMasterCredentials(),
                RECEIVING_ACCOUNT_ADDRESS,
                BigDecimal.ONE,
                Convert.Unit.MWEI
        ).send();
        countDownLatch.await();
    }

    @Test
    public void listenForNotComingTransfer_ShouldNotBeTransfer() throws Exception {

    }
}
