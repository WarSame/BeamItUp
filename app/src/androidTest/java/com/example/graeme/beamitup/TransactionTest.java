package com.example.graeme.beamitup;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import com.example.graeme.beamitup.request.Request;
import com.example.graeme.beamitup.transaction.SendTransactionTask;
import com.example.graeme.beamitup.transaction.Transaction;
import com.example.graeme.beamitup.wallet.Wallet;

import org.junit.BeforeClass;
import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

import java.io.InputStream;
import java.util.Scanner;

import static com.example.graeme.beamitup.BeamItUp.INFURA_URL;

public class TransactionTest {
    private static final String TAG = "SendTransactionsTaskTest";
    private static Context appContext;
    private static final String TO_ADDRESS = "0x31B98D14007bDEe637298086988A0bBd31184523";
    private static final String TRANSACTION_VALUE = "0.01";
    private static final String FILL_EMPTY_WALLET_VALUE = "0.5";
    private static final String SECRETS_FILE = "eth.secrets";
    private static Wallet emptyWallet;
    private static Wallet filledWallet;

    @BeforeClass
    public static void setUpOneTime() throws Exception{
        appContext = InstrumentationRegistry.getTargetContext();

        emptyWallet = new Wallet.WalletBuilder()
                .nickname("my empty wallet")
                .context(appContext)
                .isUserAuthenticationRequired(false)
                .build();

        filledWallet = new Wallet.WalletBuilder()
                .nickname("my filled wallet")
                .context(appContext)
                .isUserAuthenticationRequired(false)
                .build();
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
        Web3j web3j = Web3jFactory.build(new HttpService(INFURA_URL));

        SendTransactionTask sendTransactionTask = new SendTransactionTask(web3j);

        sendTransactionTask.execute(
                new Transaction(
                    TO_ADDRESS,
                    TRANSACTION_VALUE,
                    emptyWallet.retrieveCredentials()
                )
        ).get();
    }

    @Test
    public void sendFundsFromNotEmptyWallet_ShouldBeFilledTransactionReceipt() throws Exception {
        fillWallet(filledWallet.retrieveCredentials());
        Intent sendFromEmptyWalletIntent = new Intent(appContext, SendTransactionTask.class);
        Request sendFromEmptyWalletRequest = new Request(emptyWallet.getAddress(), TRANSACTION_VALUE);
        sendFromEmptyWalletIntent.putExtra("request", sendFromEmptyWalletRequest);
        appContext.startService(sendFromEmptyWalletIntent);
    }

    private void fillWallet(Credentials credentials) throws Exception {
    }
}
