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
    private static String gasPrice;

    @BeforeClass
    public static void oneTimeSetUp() throws Exception {
        DetermineGasPriceTask task = new DetermineGasPriceTask(gasPrice->{});
        task.execute();
        gasPrice = task.get();
    }

    @Test
    public void checkGasPrice(){
        Log.i(TAG, "Gas price: " + gasPrice);
        assertTrue( gasPrice.compareTo("0") != 0 );
    }
}