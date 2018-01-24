package com.example.graeme.beamitup;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import org.junit.BeforeClass;
import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;

public class WalletTest {
    private static final String TAG = "WalletTest";
    private static final String TO_ADDRESS = "0x31B98D14007bDEe637298086988A0bBd31184523";

    private static Context appContext;
    private static String walletLocation;

    @BeforeClass
    public static void setUpOneTime() throws Exception{
        appContext = InstrumentationRegistry.getTargetContext();
        Session.createSession();

        File walletsDir = new File(appContext.getFilesDir(), "/wallets");
        Log.i(TAG, "files dir " + appContext.getFilesDir());

        if (!walletsDir.exists()){
            Log.i(TAG, "walletsDir not found");
            if ( walletsDir.mkdirs() ){
                Log.i(TAG, "walletsDir made: " + walletsDir.getAbsolutePath());
            }
            else {
                Log.i(TAG, "walletsDir not made");
                throw new FileNotFoundException();
            }
        }

        String walletName = WalletUtils.generateLightNewWalletFile(
                "somepass",
                walletsDir
        );
        walletLocation = walletsDir + "/" + walletName;
        Log.i(TAG, "walletLocation: " + walletName);
    }

    @Test
    public void loadWallet() throws Exception{
        Credentials credentials = WalletUtils.loadCredentials("somepass", new File(walletLocation));

        TransactionReceipt transactionReceipt = Transfer.sendFunds(
                Session.getWeb3j(),
                credentials,
                TO_ADDRESS,
                new BigDecimal("0.001"),
                Convert.Unit.ETHER
        ).send();

        System.out.println(transactionReceipt.getFrom());
    }
}
