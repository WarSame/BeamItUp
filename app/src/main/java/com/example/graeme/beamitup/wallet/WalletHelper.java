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
    public static Credentials retrieveCredentials(Eth eth, File walletFile) throws Exception {
        Log.i(TAG, "Wallet file location: " + walletFile);
        String longPassword = Encryption.decryptWalletPassword(
                eth.getEncryptedLongPassword(),
                eth.getIV(),
                eth.getWalletName()
        );
        Log.i(TAG, "Wallet retrieved");
        return WalletUtils.loadCredentials(longPassword, walletFile);
    }

    public static String generateWallet(String longPassword, File walletDir) throws Exception {
        return WalletUtils.generateLightNewWalletFile(longPassword, walletDir);
    }

    public static Eth insertWalletEth(String walletName, String longPassword, String nickname, EthDbAdapter ethDbAdapter, File walletFile) throws Exception {
        EncryptedWallet encryptedWallet = Encryption.encryptWalletPassword(walletName, longPassword);

        Credentials credentials = WalletHelper.retrieveCredentials(
                walletFile,
                encryptedWallet.getEncryptedLongPassword(),
                encryptedWallet.getIV(),
                walletName
        );

        Eth eth = new Eth(
                nickname,
                credentials.getAddress(),
                walletName,
                encryptedWallet.getEncryptedLongPassword(),
                encryptedWallet.getIV()
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
