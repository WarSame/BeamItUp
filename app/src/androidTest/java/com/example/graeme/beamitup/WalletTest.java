package com.example.graeme.beamitup;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import com.example.graeme.beamitup.request.Request;
import com.example.graeme.beamitup.wallet.Wallet;

import org.junit.BeforeClass;
import org.junit.Test;
import org.web3j.crypto.Credentials;

import java.io.File;
import java.io.InputStream;
import java.util.Scanner;

public class WalletTest {
    private static final String TAG = "WalletTest";
    private static final String TO_ADDRESS = "0x31B98D14007bDEe637298086988A0bBd31184523";
    private static final String TRANSACTION_VALUE = "0.01";
    private static final String FILL_EMPTY_WALLET_VALUE = "0.5";
    private static final String SECRETS_FILE = "eth.secrets";

    private static Context appContext;

    @BeforeClass
    public static void setUpOneTime() throws Exception{
        appContext = InstrumentationRegistry.getTargetContext();

        Wallet emptyWallet = new Wallet.WalletBuilder()
                .nickname("my empty wallet")
                .context(appContext)
                .build();

        Wallet filledWallet = new Wallet.WalletBuilder()
                .nickname("my filled wallet")
                .context(appContext)
                .build();

        Request request = new Request(TO_ADDRESS, TRANSACTION_VALUE);
    }

    private static Credentials retrieveMasterCredentials() throws  Exception {
        return Credentials.create(retrieveMasterPrivateKey());
    }

    private static String retrieveMasterPrivateKey() throws Exception {
        Context testContext = InstrumentationRegistry.getInstrumentation().getContext();
        InputStream testInput = testContext.getAssets().open(SECRETS_FILE);
        Scanner in = new Scanner(testInput);
        return in.next();
    }

    @Test
    public void sendFundsFromEmptyWallet_ShouldBeNullTransactionReceipt() throws Exception{
    }

    @Test
    public void sendFundsFromNotEmptyWallet_ShouldBeFilledTransactionReceipt() throws Exception {
    }

    private void fillWallet(Credentials credentials) throws Exception {

    }
}