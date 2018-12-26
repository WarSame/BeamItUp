package com.example.graeme.beamitup;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ServiceTestRule;
import android.util.Log;

import com.example.graeme.beamitup.request.Request;
import com.example.graeme.beamitup.transaction.SendTransactionService;
import com.example.graeme.beamitup.transaction.Transaction;
import com.example.graeme.beamitup.wallet.Wallet;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeoutException;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.web3j.tx.Transfer.sendFunds;

public class SendTransactionServiceTest {
    private static final String TAG = "SendTransactionsTaskTest";
    private static Context appContext;
    private static final String TO_ADDRESS = "0x31B98D14007bDEe637298086988A0bBd31184523";
    private static final String TRANSACTION_VALUE = "0.01";
    private static Web3j web3j;
    @ClassRule
    public static final ServiceTestRule serviceTestRule = new ServiceTestRule();
    private static SendTransactionService service;

    @BeforeClass
    public static void setUpOneTime() throws TimeoutException {
        appContext = InstrumentationRegistry.getTargetContext();

        web3j = Web3j.build(new HttpService(BeamItUp.INFURA_URL));

        Intent intent = new Intent(appContext, SendTransactionService.class);
        service = ((
                SendTransactionService.SendTransactionBinder)
                serviceTestRule.bindService(intent)
        ).getService();

    }

    @Test
    public void sendFundsFromEmptyWallet_ShouldBeNullTransactionReceipt() throws Exception {
        Wallet emptyWallet = new Wallet.WalletBuilder()
                .nickname("my empty wallet")
                .context(appContext)
                .isUserAuthenticationRequired(false)
                .build();

        Request request = new Request(TO_ADDRESS, TRANSACTION_VALUE);
        Transaction transaction = new Transaction(emptyWallet, request);

        CountDownLatch countDownLatch = new CountDownLatch(1);
        Log.i(TAG, "Transaction = " + transaction);
        Log.i(TAG, "Service = " + service);
        service.sendTransaction(transaction, (receipt)->{
            if (receipt == null){
                Log.i(TAG, "Receipt is null");
            }
            else {
                Log.i(TAG, "Receipt is not null");
            }
            assertNull(receipt);
            countDownLatch.countDown();
        });
        countDownLatch.await();
    }

    @Test
    public void sendFundsFromNotEmptyWallet_ShouldBeFilledTransactionReceipt() throws Exception {
        Wallet filledWallet = new Wallet.WalletBuilder()
                .nickname("my filled wallet")
                .context(appContext)
                .isUserAuthenticationRequired(false)
                .build();

        EthGetBalance ethGetBalance = web3j.ethGetBalance(
                filledWallet.getAddress()
                , DefaultBlockParameterName.LATEST
        )
                .sendAsync().get();

        Log.d(TAG, "Funds in wallet " + ethGetBalance.getBalance() );

        FillWallet.fillWallet(filledWallet.getAddress(), web3j);

        Request request = new Request(TO_ADDRESS, TRANSACTION_VALUE);
        Transaction transaction = new Transaction(filledWallet, request);

        Log.i(TAG, "Transaction = " + transaction);
        Log.i(TAG, "Service = " + service);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        service.sendTransaction(transaction, (receipt)->{
            if (receipt == null){
                Log.i(TAG, "Receipt is null");
            }
            else {
                Log.i(TAG, "Receipt is not null");
            }
            assertNotNull(receipt);
            countDownLatch.countDown();
        });
        countDownLatch.await();
    }

}
