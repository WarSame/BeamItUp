package com.example.graeme.beamitup.eth_tasks;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.graeme.beamitup.BeamItUp;
import com.example.graeme.beamitup.Encryption;
import com.example.graeme.beamitup.LandingPageActivity;
import com.example.graeme.beamitup.R;
import com.example.graeme.beamitup.eth.DaoSession;
import com.example.graeme.beamitup.eth.Eth;
import com.example.graeme.beamitup.eth.EthDao;
import com.example.graeme.beamitup.wallet.EncryptedWallet;
import com.example.graeme.beamitup.wallet.WalletHelper;

import org.web3j.crypto.Credentials;

import java.io.File;

public class GenerateWalletService extends IntentService {
    private static final String TAG = "GenerateWalletService";

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null){
            Log.e(TAG, "Null intent received");
            return;
        }

        String longPassword = Encryption.generateLongRandomString();

        try {
            String nickname = intent.getStringExtra("nickname");
            File walletDir = WalletHelper.getWalletDir(this);
            String walletName = WalletHelper.generateWallet(longPassword, walletDir);
            handleWalletCreation(walletName, nickname, longPassword);
            onCreateEthSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            onCreateEthFail();
        }
    }

    public GenerateWalletService(){
        super("GenerateWalletService");
    }

    private void handleWalletCreation(String walletName, String nickname, String longPassword) throws Exception{
        File walletFile = WalletHelper.getWalletFile(this, walletName);
        Credentials credentials = WalletHelper.retrieveCredentials(walletFile, longPassword);

        EncryptedWallet encryptedWallet = Encryption.encryptWalletPassword(walletName, longPassword);

        Eth eth = new Eth()
            .setNickname(nickname)
            .setAddress(credentials.getAddress())
            .setWalletName(walletName)
            .setEncryptedLongPassword(encryptedWallet.getEncryptedLongPassword())
            .setIV(encryptedWallet.getIV());

        insertEth(eth);
    }


    private void insertEth(Eth eth){
        DaoSession daoSession = ((BeamItUp)getApplication()).getDaoSession();
        EthDao ethDao = daoSession.getEthDao();
        ethDao.insert(eth);
        Log.d(TAG, "Inserted new eth " + eth.getId());
    }


    private void onCreateEthSuccess(){
        Log.i(TAG, "Eth created");
    }

    private void onCreateEthFail(){
        Log.i(TAG, "Eth creation failed");
    }

}
