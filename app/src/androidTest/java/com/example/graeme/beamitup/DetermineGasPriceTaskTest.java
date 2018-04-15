package com.example.graeme.beamitup;

import android.util.Log;

import com.example.graeme.beamitup.eth_tasks.DetermineGasPriceTask;
import com.example.graeme.beamitup.eth_tasks.DetermineGasPriceTask.DetermineGasPriceResponse;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.web3j.protocol.core.methods.response.EthGasPrice;

import java.math.BigInteger;

import static org.junit.Assert.assertTrue;

public class DetermineGasPriceTaskTest {
    private static final String TAG = "DetermineGasPriceTaskTest";
    private static EthGasPrice ethGasPrice;

    @BeforeClass
    public static void oneTimeSetUp() throws Exception {
        DetermineGasPriceResponse response = gasPrice -> {
            //Empty because we are running sync
        };
        Session.createSession();//Empty session for testing

        DetermineGasPriceTask task = new DetermineGasPriceTask(response);
        task.execute();
        ethGasPrice = task.get();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void checkGasPrice(){
        Log.i(TAG, "Gas price: " + ethGasPrice.getGasPrice());
        assertTrue( ethGasPrice.getGasPrice().compareTo(new BigInteger(("0"))) != 0 );
    }
}