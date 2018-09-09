package com.example.graeme.beamitup;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ServiceTestRule;
import android.util.Log;

import com.example.graeme.beamitup.transaction.SendTransactionService;
import com.example.graeme.beamitup.transaction.SendTransactionService.SendTransactionBinder;
import com.example.graeme.beamitup.transaction.SendTransactionService.InsufficientFundsException;
import com.example.graeme.beamitup.transaction.Transaction;
import com.example.graeme.beamitup.wallet.Wallet;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Scanner;

import static junit.framework.TestCase.assertTrue;
import static org.web3j.tx.Transfer.sendFunds;

public class SendTransactionServiceTest {
    private static final String TAG = "SendTransactionsTaskTest";
    private static Context appContext;
    private static final String TO_ADDRESS = "0x31B98D14007bDEe637298086988A0bBd31184523";
    private static final String TRANSACTION_VALUE = "0.01";
    private static final String SECRETS_FILE = "eth.secrets";
    private static Web3j web3j;
    @Rule
    public final ServiceTestRule serviceTestRule = new ServiceTestRule();

    @BeforeClass
    public static void setUpOneTime() {
        appContext = InstrumentationRegistry.getTargetContext();

        web3j = Web3jFactory.build(new HttpService(BeamItUp.INFURA_URL));
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

    private SendTransactionService service;
    private boolean bound = false;
    @Test(expected = InsufficientFundsException.class)
    public void sendFundsFromEmptyWallet_ShouldBeNullTransactionReceipt() throws Exception, InsufficientFundsException {
        Wallet emptyWallet = new Wallet.WalletBuilder()
                .nickname("my empty wallet")
                .context(appContext)
                .isUserAuthenticationRequired(false)
                .build();

        TransactionReceipt receipt = sendTestTransfer(emptyWallet);
        assertTrue(receipt == null);
    }

    @Test
    public void sendFundsFromNotEmptyWallet_ShouldBeFilledTransactionReceipt() throws Exception, InsufficientFundsException {
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

        fillWallet(filledWallet.getAddress());

        TransactionReceipt receipt = sendTestTransfer(filledWallet);
        assertTrue(receipt != null);
    }

    private TransactionReceipt sendTestTransfer(Wallet wallet) throws Exception, InsufficientFundsException{
        Intent intent = new Intent(appContext, SendTransactionService.class);
        Credentials walletCredentials = wallet.retrieveCredentials();
        Transaction transaction = new Transaction(TO_ADDRESS, TRANSACTION_VALUE, walletCredentials);
        ServiceConnection connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                SendTransactionBinder sendTransactionBinder = (SendTransactionBinder) iBinder;
                service = sendTransactionBinder.getService();
                bound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                bound = false;
            }
        };
        if (!bound) {
            serviceTestRule.bindService(intent, connection, Context.BIND_AUTO_CREATE);
        }
        return service.sendTransaction(transaction);
    }

    private static void fillWallet(String toAddress) throws Exception {
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
}
