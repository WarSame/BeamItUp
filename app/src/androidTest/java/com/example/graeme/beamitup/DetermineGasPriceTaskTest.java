package com.example.graeme.beamitup;

import android.util.Log;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.web3j.protocol.core.methods.response.EthGasPrice;

import static org.junit.Assert.*;
import com.example.graeme.beamitup.DetermineGasPriceTask.*;

import java.math.BigInteger;

public class DetermineGasPriceTaskTest {
    private static final String TAG = "DetermineGasPriceTaskTest";
    private static EthGasPrice ethGasPrice;

    @BeforeClass
    public static void oneTimeSetUp() throws Exception {
        DetermineGasPriceResponse response = gasPrice -> {
            //Empty because we are running sync
        };
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
        assertTrue( ethGasPrice.getGasPrice().compareTo(new BigInteger(("20000000000"))) == 0 );
    }
}