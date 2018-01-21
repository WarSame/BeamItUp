package com.example.graeme.beamitup;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ServiceTestRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class TransferSenderServiceTest {
    private static final String TAG = "TransferSenderServiceTest";
    private Context appContext;

    @Rule
    public final ServiceTestRule serviceTestRule = new ServiceTestRule();

    @Before
    public void setUp() throws Exception {
        appContext = InstrumentationRegistry.getTargetContext();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void send() throws Exception {
        String senderPrivateKey = "someprivatekey";
        String receiverAddress = "0x31B98D14007bDEe637298086988A0bBd31184523";

        Intent transferSenderServiceIntent = new Intent(appContext, TransferSenderService.class);
        transferSenderServiceIntent.putExtra("senderPrivateKey", senderPrivateKey);
        transferSenderServiceIntent.putExtra("receiverAddress", receiverAddress);
        serviceTestRule.startService(transferSenderServiceIntent);
    }

}