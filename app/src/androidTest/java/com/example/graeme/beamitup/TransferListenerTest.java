package com.example.graeme.beamitup;

import android.content.Context;

import com.example.graeme.beamitup.listener.TransferListener;

import org.junit.BeforeClass;
import org.junit.Test;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.websocket.WebSocketService;

public class TransferListenerTest {
    private static final String TAG = "TransferListenerTest";
    private static final String INFURA_URL = "wss://rinkeby.infura.io/ws";
    private static Context appContext;

    private static final String RECEIVING_ACCOUNT_ADDRESS = "";
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
        TransferListener transferListener = new TransferClient(web3j);
        transferListener.subscribe(RECEIVING_ACCOUNT_ADDRESS);
    }

    @Test
    public void listenForNotComingTransfer_ShouldNotBeTransfer() throws Exception {

    }
}
