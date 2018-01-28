package com.example.graeme.beamitup;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import com.example.graeme.beamitup.eth.WalletHelper;
import com.example.graeme.beamitup.request.FulfillRequestTask;
import com.example.graeme.beamitup.request.Request;

import org.junit.BeforeClass;
import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import static junit.framework.Assert.assertTrue;

public class WalletTest {
    private static final String TAG = "WalletTest";
    private static final String TO_ADDRESS = "0x31B98D14007bDEe637298086988A0bBd31184523";
    private static final String WALLET_PASSWORD = "somepass";
    private static final String TRANSACTION_VALUE = "0.01";

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

    @Test
    public void sendFundsFromEmptyWallet_ShouldBeNullTransactionReceipt() throws Exception{
        FulfillRequestTask fulfillRequestTask = new FulfillRequestTask(
                Session.getWeb3j(),
                credentials,
                sendTransactionResponse
        );
        Request request = new Request(TO_ADDRESS, TRANSACTION_VALUE);
        fulfillRequestTask.execute(request);
        TransactionReceipt transactionReceipt = fulfillRequestTask.get();
        assertTrue(transactionReceipt == null);
    }

    private SendTransactionTask.SendTransactionResponse sendTransactionResponse = (response) -> {
        Log.i(TAG, "Received transaction response");
    };
}
