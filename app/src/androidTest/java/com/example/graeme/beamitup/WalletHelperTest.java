package com.example.graeme.beamitup;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import com.example.graeme.beamitup.eth_tasks.SendTransactionTask;
import com.example.graeme.beamitup.eth_tasks.FulfillRequestTask;
import com.example.graeme.beamitup.request.Request;
import com.example.graeme.beamitup.wallet.WalletHelper;

import org.junit.BeforeClass;
import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.File;

import static com.example.graeme.beamitup.BeamItUp.getWeb3j;
import static junit.framework.Assert.assertTrue;

public class WalletHelperTest {
    private static final String TAG = "WalletHelperTest";
    private static final String TO_ADDRESS = "0x31B98D14007bDEe637298086988A0bBd31184523";
    private static final String TRANSACTION_VALUE = "0.01";
    private static final String FILL_EMPTY_WALLET_VALUE = "0.5";
    private static String emptyWalletName;
    private static String filledWalletName;
    private static Request request;

    private static Context appContext;

    @BeforeClass
    public static void setUpOneTime() throws Exception{
        appContext = InstrumentationRegistry.getTargetContext();

        File walletDir = WalletHelper.getWalletDir(appContext);
        emptyWalletName = WalletHelper.generateWallet("", walletDir);
        filledWalletName = WalletHelper.generateWallet("", walletDir);
        Log.i(TAG, "walletName: " + emptyWalletName);
        Log.i(TAG, "other wallet name: " + filledWalletName);

        request = new Request(TO_ADDRESS, TRANSACTION_VALUE);
    }

    @Test
    public void sendFundsFromEmptyWallet_ShouldBeNullTransactionReceipt() throws Exception{
        File walletFile = WalletHelper.getWalletFile(appContext, emptyWalletName);
        String longPassword = "";
        Credentials credentials = WalletHelper.retrieveCredentials(
                walletFile,
                longPassword
        );
        Log.i(TAG, "credentials address: " + credentials.getAddress());

        FulfillRequestTask fulfillRequestTask = new FulfillRequestTask(
                getWeb3j(),
                credentials,
                sendTransactionResponse
        );
        fulfillRequestTask.execute(request);
        TransactionReceipt transactionReceipt = fulfillRequestTask.get();
        assertTrue(transactionReceipt == null);
    }

    @Test
    public void sendFundsFromNotEmptyWallet_ShouldBeFilledTransactionReceipt() throws Exception {
        Credentials credentials = FulfillRequestTaskTest.retrieveMasterCredentials();
        fillEmptyWallet(credentials);
        File walletFile = WalletHelper.getWalletFile(appContext, filledWalletName);
        String longPassword = "";
        Credentials filledCredentials = WalletHelper.retrieveCredentials(
                walletFile,
                longPassword
        );

        Request fromFilledWalletRequest = new Request(credentials.getAddress(), TRANSACTION_VALUE);
        FulfillRequestTask fulfillRequestTask = new FulfillRequestTask(
                getWeb3j(),
                filledCredentials,
                sendTransactionResponse
        );
        fulfillRequestTask.execute(fromFilledWalletRequest);
        TransactionReceipt transactionReceipt = fulfillRequestTask.get();
        assertTrue(transactionReceipt != null);
    }

    private void fillEmptyWallet(Credentials credentials) throws Exception {
        Request fillEmptyWalletRequest = new Request("", FILL_EMPTY_WALLET_VALUE);

        FulfillRequestTask fulfillRequestTask = new FulfillRequestTask(
                getWeb3j(),
                credentials,
                sendTransactionResponse
        );
        fulfillRequestTask.execute(fillEmptyWalletRequest);
        fulfillRequestTask.get();
    }

    private SendTransactionTask.SendTransactionResponse sendTransactionResponse = (response) -> {
        Log.i(TAG, "Received transaction response");
    };
}
