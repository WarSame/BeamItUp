package com.example.graeme.beamitup.eth_tasks;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.graeme.beamitup.BeamItUp;
import com.example.graeme.beamitup.eth.DaoSession;
import com.example.graeme.beamitup.eth.Eth;
import com.example.graeme.beamitup.eth.EthDao;
import com.example.graeme.beamitup.request.Request;
import com.example.graeme.beamitup.transaction.Transaction;
import com.example.graeme.beamitup.wallet.WalletHelper;

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

        long ethID = request.getFromID();
        Eth eth = retrieveEth(ethID);

        TransactionReceipt receipt;
        try {
            Credentials credentials = retrieveWallet(eth);
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

    private Eth retrieveEth(long ethID){
        Log.i(TAG, "Retrieving eth: " + ethID);
        DaoSession daoSession = ((BeamItUp) getApplication()).getDaoSession();
        EthDao ethDao = daoSession.getEthDao();
        return ethDao.load(ethID);
    }

    private Credentials retrieveWallet(Eth eth) throws Exception {
        File walletFile = WalletHelper.getWalletFile(this, eth.getWalletName());
        return WalletHelper.retrieveCredentials(eth, walletFile);
    }
}
