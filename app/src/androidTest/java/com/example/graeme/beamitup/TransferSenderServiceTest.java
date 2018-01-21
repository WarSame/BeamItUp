package com.example.graeme.beamitup;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ServiceTestRule;
import android.util.Log;
import com.example.graeme.beamitup.TransferSenderService.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.util.concurrent.Future;

import static org.junit.Assert.*;

public class TransferSenderServiceTest {
    private static final String TAG = "TransferSenderServiceTest";
    private Context appContext;
    private boolean bound = false;
    private TransferSenderService transferSenderService;

    @Rule
    public ServiceTestRule serviceTestRule = new ServiceTestRule();

    @Before
    public void setUp() throws Exception {
        appContext = InstrumentationRegistry.getTargetContext();
        Intent transferSenderIntent = new Intent(appContext, TransferSenderService.class);
        serviceTestRule.bindService(transferSenderIntent, connection, Context.BIND_AUTO_CREATE);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void send() throws Exception {
        String senderPrivateKey = "";
        String receiverAddress = "0x31B98D14007bDEe637298086988A0bBd31184523".toLowerCase();
        Credentials credentials = Credentials.create(senderPrivateKey);

        Thread.sleep(500);
        if (bound){
            Log.d(TAG, "Service is bound");
            Future<TransactionReceipt> future = transferSenderService.send(credentials, receiverAddress);
            TransactionReceipt tr = future.get();
            Log.i(TAG, "receiver address: " + receiverAddress);
            Log.i(TAG, "tr to: " + tr.getTo());
            assertTrue( tr.getTo().equals(receiverAddress) );
        }
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            Log.i(TAG, "Service is connected.");
            TransferSenderBinder binder = (TransferSenderBinder) service;
            transferSenderService = binder.getService();
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "Service is disconnected.");
            bound = false;
        }
    };

}