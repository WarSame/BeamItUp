package com.example.graeme.beamitup.wallet;

import android.content.Context;
import android.util.Log;

import com.example.graeme.beamitup.Encryption;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import java.io.File;
import java.io.IOException;

public class WalletHelper {
    private static final String TAG = "WalletHelper";
    private static final String WALLET_DIR_RELATIVE_PATH = "/wallets";

    public static Credentials retrieveCredentials(Context context, long ethID) throws IOException, CipherException {
        //Retrieve encrypted long password from DB with IV, decrypt using keystore
        Wallet wallet = retrieveWalletForEth(context, ethID);
        File walletFile = new File( getWalletDir(context) + "/" + wallet.getWalletName());
        Log.i(TAG, "Wallet file location: " + walletFile);
        return WalletUtils.loadCredentials(wallet.getLongPassword(), walletFile);
    }

    private static Wallet retrieveWalletForEth(Context context, long ethID){
        EncryptedWallet encryptedWallet = retrieveEncryptedWalletFromDB(context, ethID);
        byte[] encryptedLongPassword = encryptedWallet.getEncryptedLongPassword();
        byte[] IV = encryptedWallet.getIV();
        String walletName = encryptedWallet.getWalletName();
        String longPassword = Encryption.decryptWalletPassword(encryptedLongPassword, IV, walletName);
        return new Wallet(walletName, longPassword);
    }

    private static EncryptedWallet retrieveEncryptedWalletFromDB(Context context, long ethID) {
        WalletDbAdapter walletDB = new WalletDbAdapter(context);
        EncryptedWallet encryptedWallet = walletDB.retrieveEncryptedWalletByEthID(ethID);
        walletDB.close();
        return encryptedWallet;
    }

    public static String generateWallet(Context context, long ethID) throws Exception {
        byte[] IV = Encryption.generateIV();
        String longPassword = Encryption.generateLongRandomString();
        String walletName = WalletUtils.generateLightNewWalletFile(longPassword, getWalletDir(context));
        byte[] encryptedLongPassword = Encryption.encryptWalletPassword(walletName, longPassword, IV);
        addEncryptedWalletToDB(context, walletName, IV, encryptedLongPassword, ethID);
        return walletName;
    }

    private static void addEncryptedWalletToDB(Context context, String walletName, byte[] IV, byte[] encryptedLongPassword, long ethID) {
        EncryptedWallet encryptedWallet = new EncryptedWallet(encryptedLongPassword, IV, walletName);
        WalletDbAdapter walletDB = new WalletDbAdapter(context);
        walletDB.createEncryptedWallet(encryptedWallet, ethID);
        walletDB.close();
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
