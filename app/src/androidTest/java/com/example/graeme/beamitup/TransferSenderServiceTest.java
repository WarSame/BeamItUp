package com.example.graeme.beamitup;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ServiceTestRule;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class TransferSenderServiceTest {
    private static final String TAG = "TransferSenderServiceTest";
    private TransferSenderService transferSenderService;
    private boolean bound = false;

    @Rule
    public final ServiceTestRule serviceTestRule = new ServiceTestRule();

    @Before
    public void setUp() throws Exception {
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
        Context appContext = InstrumentationRegistry.getTargetContext();
        Intent transferSenderIntent = new Intent(appContext, TransferSenderService.class);
        IBinder binder = serviceTestRule.bindService(transferSenderIntent);
        transferSenderService = ((TransferSenderService.TransferSenderBinder) binder).getService();
        assertTrue(transferSenderService.getValue() == 5);
    }

    @Test
    public void send() throws Exception {
    }

}