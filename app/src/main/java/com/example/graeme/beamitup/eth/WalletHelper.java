package com.example.graeme.beamitup.eth;

import android.content.Context;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class WalletHelper {
    private static final String WALLET_DIR_RELATIVE_PATH = "/wallets";
    public static Credentials retrieveCredentials(Context context, String password, String walletName) throws IOException, CipherException {
        File walletFile = new File( getWalletDir(context) + "/" + walletName);
        return WalletUtils.loadCredentials(password, walletFile);
    }

    public static String generateWallet(Context context, String password) throws CipherException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, IOException {
        return WalletUtils.generateLightNewWalletFile(password, getWalletDir(context));
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
