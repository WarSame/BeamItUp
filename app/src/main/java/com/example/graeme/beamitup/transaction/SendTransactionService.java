package com.example.graeme.beamitup.transaction;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.graeme.beamitup.BeamItUp;
import com.example.graeme.beamitup.wallet.DaoSession;
import com.example.graeme.beamitup.wallet.Wallet;
import com.example.graeme.beamitup.wallet.WalletDao;
import com.example.graeme.beamitup.request.Request;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Convert;

import java.io.File;
import java.math.BigDecimal;

public class SendTransactionService extends IntentService {
    private static final String TAG = "SendTransactionService";

    public SendTransactionService(){
        super("SendTransactionService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null){
            Log.e(TAG, "Null intent received");
            return;
        }

        Web3j web3j = ((BeamItUp)getApplication()).getWeb3j();
        Request request = (Request) intent.getSerializableExtra("request");

        long walletID = request.getFromID();
        Wallet wallet = retrieveWallet(walletID);

        TransactionReceipt receipt;
        try {
            Credentials credentials = retrieveWallet(wallet);
            Log.d(TAG, "Sender address: " + credentials.getAddress());

            receipt = org.web3j.tx.Transfer.sendFunds(
                    web3j,
                    credentials,
                    request.getToAddress(),
                    new BigDecimal(request.getAmount()),
                    Convert.Unit.ETHER
            ).send();

            Log.d(TAG, "Transaction from: " + receipt.getFrom());
            Log.d(TAG, "Transaction to: " + receipt.getTo());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Wallet retrieveWallet(long walletID){
        Log.i(TAG, "Retrieving wallet: " + walletID);
        DaoSession daoSession = ((BeamItUp) getApplication()).getDaoSession();
        WalletDao walletDao = daoSession.getWalletDao();
        return walletDao.load(walletID);
    }

    private Credentials retrieveWallet(Wallet wallet) throws Exception {
        File walletFile = Wallet.getWalletFile(this, wallet.getWalletName());
        return Wallet.retrieveCredentials(wallet, walletFile);
    }
}
