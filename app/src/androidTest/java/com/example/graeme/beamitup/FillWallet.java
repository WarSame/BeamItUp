package com.example.graeme.beamitup;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.utils.Convert;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Scanner;

import static org.web3j.tx.Transfer.sendFunds;

class FillWallet {
    private static final String TAG = "FillWallet";
    private static final String SECRETS_FILE = "eth.secrets";

    static void fillWallet(String toAddress, Web3j web3j) throws Exception {
        Credentials credentials = retrieveMasterCredentials();

        Log.d(TAG, "Master address: " + credentials.getAddress());
        sendFunds(
                web3j,
                credentials,
                toAddress,
                BigDecimal.ONE,
                Convert.Unit.ETHER
        ).send();
    }

    static Credentials retrieveMasterCredentials() throws  Exception {
        return Credentials.create(retrieveMasterPrivateKey());
    }

    private static String retrieveMasterPrivateKey() throws Exception {
        Context testContext = InstrumentationRegistry.getInstrumentation().getContext();
        InputStream testInput = testContext.getAssets().open(SECRETS_FILE);
        Scanner in = new Scanner(testInput);
        return in.next();
    }
}
