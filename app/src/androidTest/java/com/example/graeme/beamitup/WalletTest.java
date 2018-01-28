package com.example.graeme.beamitup;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import com.example.graeme.beamitup.eth.WalletHelper;

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
    private static final String WALLET_PASSWORD = "somepass";

    private static Context appContext;
    private static Credentials credentials;

    @BeforeClass
    public static void setUpOneTime() throws Exception{
        appContext = InstrumentationRegistry.getTargetContext();
        Session.createSession();

        String walletName = WalletHelper.generateWallet(appContext, WALLET_PASSWORD);
        Log.i(TAG, "walletName: " + walletName);

        credentials = WalletHelper.retrieveCredentials(
                appContext,
                WALLET_PASSWORD,
                walletName
        );
        Log.i(TAG, "credentials address: " + credentials.getAddress());
    }

    @Test(expected = RuntimeException.class)
    public void loadWallet() throws Exception{
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
