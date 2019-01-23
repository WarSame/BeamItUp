package com.example.graeme.beamitup.integration;

import android.util.Log;

import org.junit.BeforeClass;
import org.junit.Test;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.websocket.WebSocketService;

import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

public class WebSocketTest {
    static private final String INFURA_URL = "wss://rinkeby.infura.io/ws";
    static private final boolean INCLUDE_RAW_RESPONSES = false;
    static private Web3j web3j;

    @BeforeClass
    public static void setUpOneTime() throws Exception {
        WebSocketService webSocketService = new WebSocketService(INFURA_URL, INCLUDE_RAW_RESPONSES);
        webSocketService.connect();
        web3j = Web3j.build(webSocketService);
    }

    @Test
    public void getGasPrice_ShouldBeBigInt() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Disposable disposable = web3j
            .ethGasPrice()
            .flowable()
            .subscribe(
                    ethGasPrice -> {
                        assertNotNull(ethGasPrice);
                        countDownLatch.countDown();
                    }
            );
        assertTrue(countDownLatch.await(10, TimeUnit.SECONDS));
        disposable.dispose();
    }

}
