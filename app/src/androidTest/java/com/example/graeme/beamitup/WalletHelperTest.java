package com.example.graeme.beamitup;

import android.content.Context;
import android.database.CursorIndexOutOfBoundsException;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import com.example.graeme.beamitup.eth.Eth;
import com.example.graeme.beamitup.eth.EthDbAdapter;
import com.example.graeme.beamitup.request.FulfillRequestTask;
import com.example.graeme.beamitup.request.Request;
import com.example.graeme.beamitup.wallet.WalletHelper;

import org.junit.BeforeClass;
import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import static junit.framework.Assert.assertTrue;

public class WalletHelperTest {
    private static final String TAG = "WalletHelperTest";
    private static final String TO_ADDRESS = "0x31B98D14007bDEe637298086988A0bBd31184523";
    private static final String TRANSACTION_VALUE = "0.01";
    private static final String FILL_EMPTY_WALLET_VALUE = "0.5";
    private static Eth filledEth;
    private static Request request;

    private static Context appContext;

    @BeforeClass
    public static void setUpOneTime() throws Exception{
        appContext = InstrumentationRegistry.getTargetContext();
        Session.createSession();

        EthDbAdapter ethDB = new EthDbAdapter(appContext);
        DbAdapter.DatabaseHelper dbHelper = new DbAdapter.DatabaseHelper(appContext);
        dbHelper.onUpgrade(ethDB.db, 0, 1);//Wipe db tables

        long ACCOUNT_ID = 1;
        Eth emptyEth = WalletHelper.generateWallet(appContext, "somenick", ACCOUNT_ID);
        filledEth = WalletHelper.generateWallet(appContext, "someothernick", ACCOUNT_ID);
        Log.i(TAG, "walletName: " + emptyEth.getWalletName());
        Log.i(TAG, "other wallet name: " + filledEth.getWalletName());

        request = new Request(TO_ADDRESS, TRANSACTION_VALUE);
    }

    @Test
    public void sendFundsFromEmptyWallet_ShouldBeNullTransactionReceipt() throws Exception{
        long ETH_ID = 1;
        Credentials credentials = WalletHelper.retrieveCredentials(
                appContext,
                ETH_ID
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

    @Test
    public void sendFundsFromNotEmptyWallet_ShouldBeFilledTransactionReceipt() throws Exception {
        Credentials credentials = FulfillRequestTaskTest.retrieveMasterCredentials();
        fillEmptyWallet(credentials);
        Credentials filledCredentials = WalletHelper.retrieveCredentials(
                appContext,
                filledEth.getId()
        );

        Request fromFilledWalletRequest = new Request(credentials.getAddress(), TRANSACTION_VALUE);
        FulfillRequestTask fulfillRequestTask = new FulfillRequestTask(
                Session.getWeb3j(),
                filledCredentials,
                sendTransactionResponse
        );
        fulfillRequestTask.execute(fromFilledWalletRequest);
        TransactionReceipt transactionReceipt = fulfillRequestTask.get();
        assertTrue(transactionReceipt != null);
    }

    private void fillEmptyWallet(Credentials credentials) throws Exception {
        Request fillEmptyWalletRequest = new Request(filledEth.getAddress(), FILL_EMPTY_WALLET_VALUE);

        FulfillRequestTask fulfillRequestTask = new FulfillRequestTask(
                Session.getWeb3j(),
                credentials,
                sendTransactionResponse
        );
        fulfillRequestTask.execute(fillEmptyWalletRequest);
        fulfillRequestTask.get();
    }

    @Test(expected = CursorIndexOutOfBoundsException.class)
    public void sendFundsWithWrongEthID_ShouldBeCursorBoundException() throws Exception{
        long WRONG_ETH_ID = 56;
        WalletHelper.retrieveCredentials(
                appContext,
                WRONG_ETH_ID
        );
    }

    private SendTransactionTask.SendTransactionResponse sendTransactionResponse = (response) -> {
        Log.i(TAG, "Received transaction response");
    };
}
