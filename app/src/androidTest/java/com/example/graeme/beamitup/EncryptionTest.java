package com.example.graeme.beamitup;

import android.content.Context;
import android.security.keystore.UserNotAuthenticatedException;
import android.support.test.InstrumentationRegistry;

import com.example.graeme.beamitup.wallet.WalletHelper;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import encryption.Decryptor;
import encryption.Encryption;
import encryption.Encryptor;

import static junit.framework.Assert.assertTrue;

public class EncryptionTest {
    private static final String TAG = "EncryptionTest";
    private Context appContext;

    @Before
    public void setUp() {
        appContext = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void encryptWalletPassword_AuthenticationNotRequired_EncryptedLongPasswordIsSet() throws Exception {
        String longPassword = Encryption.generateLongRandomString();
        File walletDir = WalletHelper.getWalletDir(appContext);
        String walletName = WalletHelper.generateWallet(longPassword, walletDir);
        Encryptor encryptor = new Encryptor()
                .setUserAuthenticationRequired(false)
                .encryptWalletPassword(walletName, longPassword);
        assertTrue(encryptor.getEncryptedLongPassword() != null);
    }

    @Test(expected = UserNotAuthenticatedException.class)
    public void encryptWalletPassword_AuthenticationRequired_UserNotAuthenticatedException() throws Exception {
        String longPassword = Encryption.generateLongRandomString();
        File walletDir = WalletHelper.getWalletDir(appContext);
        String walletName = WalletHelper.generateWallet(longPassword, walletDir);
        Encryptor encryptor = new Encryptor()
                .setUserAuthenticationRequired(true)
                .encryptWalletPassword(walletName, longPassword);
        assertTrue(encryptor.getEncryptedLongPassword() != null);
    }

    @Test
    public void decryptWalletPassword_AuthenticationNotRequired_ReturnsGivenString() throws Exception {
        String longPassword = Encryption.generateLongRandomString();
        File walletDir = WalletHelper.getWalletDir(appContext);
        String walletName = WalletHelper.generateWallet(longPassword, walletDir);
        Encryptor encryptor = new Encryptor()
                .setUserAuthenticationRequired(false)
                .encryptWalletPassword(walletName, longPassword);

        String unencryptedLongPassword = new Decryptor.DecryptorBuilder()
                .setEncryptedLongPassword(encryptor.getEncryptedLongPassword())
                .setIV(encryptor.getIV())
                .setWalletName(walletName)
                .build()
                .decryptWalletPassword();

        assertTrue(unencryptedLongPassword.equals(longPassword));
    }

    @Test(expected = UserNotAuthenticatedException.class)
    public void decryptWalletPassword_AuthenticationRequired_UserNotAuthenticatedException() throws Exception {
        String longPassword = Encryption.generateLongRandomString();
        File walletDir = WalletHelper.getWalletDir(appContext);
        String walletName = WalletHelper.generateWallet(longPassword, walletDir);

        Encryptor encryptor = new Encryptor()
                .setUserAuthenticationRequired(true)
                .encryptWalletPassword(walletName, longPassword);

        String unencryptedLongPassword = new Decryptor.DecryptorBuilder()
                .setEncryptedLongPassword(encryptor.getEncryptedLongPassword())
                .setIV(encryptor.getIV())
                .setWalletName(walletName)
                .build()
                .decryptWalletPassword();

        assertTrue(unencryptedLongPassword.equals(longPassword));
    }

}