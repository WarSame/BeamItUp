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

    public static Eth generateWallet(File walletDir, String nickname, EthDbAdapter ethDbAdapter) throws Exception {
        String longPassword = Encryption.generateLongRandomString();
        String walletName = WalletUtils.generateLightNewWalletFile(longPassword, walletDir);
        EncryptedWallet encryptedWallet = Encryption.encryptWalletPassword(walletName, longPassword);
        byte[] IV = encryptedWallet.getIV();
        byte[] encryptedLongPassword = encryptedWallet.getEncryptedLongPassword();

        File walletFile = new File(walletDir + "/" + walletName);

        Credentials credentials = retrieveCredentials(walletFile, encryptedLongPassword, IV, walletName);
        String address = credentials.getAddress();

        Eth eth = new Eth(
                nickname,
                address,
                walletName,
                encryptedLongPassword,
                IV
        );
        long ethID = ethDbAdapter.createEth(eth);
        eth.setId(ethID);

        return eth;
    }

    private static Credentials retrieveCredentials(File walletFile, byte[] encryptedLongPassword, byte[] IV, String walletName) throws Exception{
        String longPassword = Encryption.decryptWalletPassword(encryptedLongPassword, IV, walletName);
        return WalletUtils.loadCredentials(longPassword, walletFile);
    }

    public static File getWalletFile(Context context, String walletName) throws Exception {
        return new File(getWalletDir(context) + "/" + walletName);
    }

    public static File getWalletDir(Context context) throws IOException {
        File walletDir = new File(context.getFilesDir() + WALLET_DIR_RELATIVE_PATH);
        if (!walletDir.exists()){
            if ( !walletDir.mkdir() ){
                throw new IOException();
            }
        }
        return walletDir;
    }


}
