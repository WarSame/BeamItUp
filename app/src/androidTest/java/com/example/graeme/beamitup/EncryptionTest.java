package com.example.graeme.beamitup;

import android.content.Context;
import android.security.keystore.UserNotAuthenticatedException;
import android.support.test.InstrumentationRegistry;

import com.example.graeme.beamitup.wallet.WalletHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static junit.framework.Assert.assertTrue;

public class EncryptionTest {
    private static final String TAG = "EncryptionTest";
    private Context appContext;

    @Before
    public void setUp() {
        appContext = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void encryptWalletPassword_AuthenticationNotRequired_Filled() throws Exception {
        String longPassword = Encryption.generateLongRandomString();
        File walletDir = WalletHelper.getWalletDir(appContext);
        String walletName = WalletHelper.generateWallet(longPassword, walletDir);
        Encryption.Encryptor encryptor = new Encryption().new Encryptor()
                .encryptWalletPassword(walletName, longPassword);
        assertTrue(encryptor.getEncryptedLongPassword() != null);
    }

    @Test(expected = UserNotAuthenticatedException.class)
    public void encryptWalletPassword_AuthenticationRequired_UserNotAuthenticatedException() throws Exception {
        String longPassword = Encryption.generateLongRandomString();
        File walletDir = WalletHelper.getWalletDir(appContext);
        String walletName = WalletHelper.generateWallet(longPassword, walletDir);
        Encryption.Encryptor encryptor = new Encryption().new Encryptor()
                .setUserAuthenticationRequired(true)
                .encryptWalletPassword(walletName, longPassword);
        assertTrue(encryptor.getEncryptedLongPassword() != null);
    }

}