package com.example.graeme.beamitup.wallet;

import android.content.Context;
import android.util.Log;

import com.example.graeme.beamitup.Encryption;
import com.example.graeme.beamitup.eth.Eth;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import java.io.File;
import java.io.IOException;

public class WalletHelper {
    private static final String TAG = "WalletHelper";
    private static final String WALLET_DIR_RELATIVE_PATH = "/wallets";

    public static Credentials retrieveCredentials(Eth eth, File walletFile) throws Exception {
        Log.i(TAG, "Wallet file location: " + walletFile);
        String longPassword = new Encryption().new Decryptor().decryptWalletPassword(
                eth.getEncryptedLongPassword(),
                eth.getIV(),
                eth.getWalletName()
        );
        Log.i(TAG, "Wallet retrieved");
        return WalletUtils.loadCredentials(longPassword, walletFile);
    }

    public static Credentials retrieveCredentials(File walletFile, String longPassword) throws Exception{
        Log.i(TAG, "Wallet file location: " + walletFile);
        Credentials credentials = WalletUtils.loadCredentials(longPassword, walletFile);
        Log.i(TAG, "Wallet retrieved");
        return credentials;
    }

    public static String generateWallet(String longPassword, File walletDir) throws Exception {
        return WalletUtils.generateLightNewWalletFile(longPassword, walletDir);
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
