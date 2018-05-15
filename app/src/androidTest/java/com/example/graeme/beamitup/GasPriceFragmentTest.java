package com.example.graeme.beamitup;

import android.util.Log;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class GasPriceFragmentTest {
    private static final String TAG = "GasPriceFragmentTest";
    private static String gasPrice;

    @BeforeClass
    public static void oneTimeSetUp() throws Exception {
    }

    @Test
    public void checkGasPrice(){
        Log.i(TAG, "Gas price: " + gasPrice);
        assertTrue( gasPrice.compareTo("0") != 0 );
    }
}