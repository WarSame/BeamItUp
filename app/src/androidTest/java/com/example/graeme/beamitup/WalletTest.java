package com.example.graeme.beamitup;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import org.junit.BeforeClass;
import org.junit.Test;
import org.web3j.crypto.WalletUtils;

import java.io.File;

public class WalletTest {
    private static final String TAG = "WalletTest";
    private static Context appContext;
    private static File walletsDir;

    @BeforeClass
    public static void setUpOneTime() throws Exception{
        appContext = InstrumentationRegistry.getTargetContext();

        walletsDir = new File(appContext.getFilesDir(), "/wallets");
        Log.i(TAG, "files dir " + appContext.getFilesDir());

        walletsDir.mkdirs();

        if (walletsDir.exists()){
            Log.i(TAG, "app file location exists");
        }
        else {
            Log.i(TAG, "app file location doesn't exist");
        }

        if (!walletsDir.exists()){
            Log.i(TAG, "walletsDir not found");
            if ( walletsDir.mkdirs() ){
                Log.i(TAG, "walletsDir made: " + walletsDir.getAbsolutePath());
            }
            else {
                Log.i(TAG, "walletsDir not made");
            }
        }

        String walletLocation = WalletUtils.generateLightNewWalletFile(
                "somepass",
                walletsDir
        );
        Log.i(TAG, "walletLocation: " + walletLocation);
    }

    @Test
    public void loadWallet() throws Exception{

    }
}
