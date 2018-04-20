package com.example.graeme.beamitup.wallet;

import android.content.Context;
import android.util.Log;

import com.example.graeme.beamitup.Encryption;
import com.example.graeme.beamitup.eth.Eth;
import com.example.graeme.beamitup.eth.EthDbAdapter;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import java.io.File;
import java.io.IOException;

public class WalletHelper {
    private static final String TAG = "WalletHelper";
    private static final String WALLET_DIR_RELATIVE_PATH = "/wallets";

    //Retrieve encrypted long password from DB with IV, decrypt using keystore
    public static Credentials retrieveCredentials(Context context, long ethID) throws Exception {
        EthDbAdapter ethDbAdapter = new EthDbAdapter(context);
        Eth eth = ethDbAdapter.retrieveEthByEthID(ethID);
        ethDbAdapter.close();
        File walletFile = getWalletFile(context, eth.getWalletName());
        Log.i(TAG, "Wallet file location: " + walletFile);
        String longPassword = Encryption.decryptWalletPassword(
                eth.getEncryptedLongPassword(),
                eth.getIV(),
                eth.getWalletName()
        );
        Log.i(TAG, "Wallet retrieved");
        return WalletUtils.loadCredentials(longPassword, walletFile);
    }

    public static Eth generateWallet(Context context, String nickname) throws Exception {
        String longPassword = Encryption.generateLongRandomString();
        String walletName = WalletUtils.generateLightNewWalletFile(longPassword, getWalletDir(context));
        EncryptedWallet encryptedWallet = Encryption.encryptWalletPassword(walletName, longPassword);
        byte[] IV = encryptedWallet.getIV();
        byte[] encryptedLongPassword = encryptedWallet.getEncryptedLongPassword();
        Credentials credentials = retrieveCredentials(context, encryptedLongPassword, IV, walletName);
        String address = credentials.getAddress();
        Eth eth = new Eth(
                nickname,
                address,
                walletName,
                encryptedLongPassword,
                IV
        );
        EthDbAdapter ethDbAdapter = new EthDbAdapter(context);
        long ethID = ethDbAdapter.createEth(eth);
        ethDbAdapter.close();
        eth.setId(ethID);
        return eth;
    }

    private static Credentials retrieveCredentials(Context context, byte[] encryptedLongPassword, byte[] IV, String walletName) throws Exception{
        String longPassword = Encryption.decryptWalletPassword(encryptedLongPassword, IV, walletName);
        File walletFile = getWalletFile(context, walletName);
        return WalletUtils.loadCredentials(longPassword, walletFile);
    }

    private static File getWalletFile(Context context, String walletName) throws Exception {
        return new File(getWalletDir(context) + "/" + walletName);
    }

    private static File getWalletDir(Context context) throws IOException {
        File walletDir = new File(context.getFilesDir() + WALLET_DIR_RELATIVE_PATH);
        if (!walletDir.exists()){
            if ( !walletDir.mkdir() ){
                throw new IOException();
            }
        }
        return walletDir;
    }


}
