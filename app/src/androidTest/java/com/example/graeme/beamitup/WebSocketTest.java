package com.example.graeme.beamitup;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import org.junit.BeforeClass;
import org.junit.Test;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.websocket.WebSocketService;

import java.net.URI;

import static junit.framework.Assert.assertNotNull;

public class WebSocketTest {
    private static final String TAG = "WebSocketTest";
    static private final String INFURA_URL = "wss://rinkeby.infura.io/ws";
    static private final boolean INCLUDE_RAW_RESPONSES = false;
    static private Web3j web3j;

    private static Context appContext;

    @BeforeClass
    public static void setUpOneTime() throws Exception {
        appContext = InstrumentationRegistry.getTargetContext();
        WebSocketService webSocketService = new WebSocketService(INFURA_URL, INCLUDE_RAW_RESPONSES);
        webSocketService.connect();
        web3j = Web3j.build(webSocketService);
    }

    @Test
    public void getGasPrice_ShouldBeBigInt() throws Exception{
        web3j
            .ethGasPrice()
            .flowable()
            .subscribe(
                    ethGasPrice -> {
                        assertNotNull(ethGasPrice);
                        Log.i(TAG, ethGasPrice.getGasPrice().toString());
                    }
            );
    }

}
