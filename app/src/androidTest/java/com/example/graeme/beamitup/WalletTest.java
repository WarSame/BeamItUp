package com.example.graeme.beamitup;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import com.example.graeme.beamitup.eth.WalletHelper;
import com.example.graeme.beamitup.request.FulfillRequestTask;
import com.example.graeme.beamitup.request.Request;

import org.junit.BeforeClass;
import org.junit.Test;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import static junit.framework.Assert.assertTrue;

public class WalletTest {
    private static final String TAG = "WalletTest";
    private static final String TO_ADDRESS = "0x31B98D14007bDEe637298086988A0bBd31184523";
    private static final String WALLET_PASSWORD = "somepass";
    private static final String WRONG_WALLET_PASSWORD = "someotherpass";
    private static final String TRANSACTION_VALUE = "0.01";
    private static String walletName;
    private static Request request;

    private static Context appContext;

    @BeforeClass
    public static void setUpOneTime() throws Exception{
        appContext = InstrumentationRegistry.getTargetContext();
        Session.createSession();

        walletName = WalletHelper.generateWallet(appContext, WALLET_PASSWORD);
        Log.i(TAG, "walletName: " + walletName);

        request = new Request(TO_ADDRESS, TRANSACTION_VALUE);
    }

    @Test
    public void sendFundsFromEmptyWallet_ShouldBeNullTransactionReceipt() throws Exception{
        Credentials credentials = WalletHelper.retrieveCredentials(
                appContext,
                WALLET_PASSWORD,
                walletName
        );
        Log.i(TAG, "credentials address: " + credentials.getAddress());

        FulfillRequestTask fulfillRequestTask = new FulfillRequestTask(
                Session.getWeb3j(),
                credentials,
                sendTransactionResponse
        );
        fulfillRequestTask.execute(request);
        TransactionReceipt transactionReceipt = fulfillRequestTask.get();
        assertTrue(transactionReceipt == null);
    }

    @Test(expected = CipherException.class)
    public void sendFundsWithWrongPassword_ShouldBeCipherException() throws Exception{
        Credentials credentials = WalletHelper.retrieveCredentials(
                appContext,
                WRONG_WALLET_PASSWORD,
                walletName
        );

        FulfillRequestTask task = new FulfillRequestTask(
                Session.getWeb3j(),
                credentials,
                sendTransactionResponse
        );
        task.execute(request);
    }

    private SendTransactionTask.SendTransactionResponse sendTransactionResponse = (response) -> {
        Log.i(TAG, "Received transaction response");
    };
}
