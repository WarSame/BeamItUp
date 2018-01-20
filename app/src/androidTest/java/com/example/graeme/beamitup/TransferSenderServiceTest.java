package com.example.graeme.beamitup;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ServiceTestRule;
import com.example.graeme.beamitup.TransferSenderService.TransferSenderBinder;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class TransferSenderServiceTest {
    private static final String TAG = "TransferSenderServiceTest";
    private TransferSenderService transferSenderService;
    private boolean bound = false;
    private Intent transferSenderIntent;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            TransferSenderBinder binder = (TransferSenderBinder) service;
            transferSenderService = binder.getService();
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
        }
    };

    @Rule
    public final ServiceTestRule serviceTestRule = new ServiceTestRule();

    @Before
    public void setUp() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        transferSenderIntent = new Intent(appContext, TransferSenderService.class);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void onBind() throws Exception {
    }

    @Test
    public void getSenderPrivateKey() throws Exception {
    }

    @Test
    public void getValue() throws Exception {
        serviceTestRule.bindService(transferSenderIntent, connection, Context.BIND_AUTO_CREATE);
        assertTrue(transferSenderService.getValue() == 5);
    }

    @Test
    public void send() throws Exception {
    }

}