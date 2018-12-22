package com.example.graeme.beamitup;

import android.content.Context;
import android.security.keystore.UserNotAuthenticatedException;
import android.support.test.InstrumentationRegistry;

import com.example.graeme.beamitup.wallet.Wallet;

import org.junit.Before;
import org.junit.Test;

import com.example.graeme.beamitup.encryption.Decryptor;

import static junit.framework.Assert.assertNotNull;
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
        Wallet wallet = new Wallet.WalletBuilder()
                .nickname("somewallet")
                .context(appContext)
                .isUserAuthenticationRequired(false)
                .build();
        assertNotNull(wallet.getEncryptedLongPassword());
    }

    @Test(expected = UserNotAuthenticatedException.class)
    public void encryptWalletPassword_AuthenticationRequired_UserNotAuthenticatedException() throws Exception {
        Wallet wallet = new Wallet.WalletBuilder()
                .nickname("somewallet")
                .context(appContext)
                .build();
        assertNotNull(wallet.getEncryptedLongPassword());
    }

    @Test
    public void decryptWalletPassword_AuthenticationNotRequired_ReturnsGivenString() throws Exception {
        Wallet wallet = new Wallet.WalletBuilder()
                .nickname("somewallet")
                .context(appContext)
                .isUserAuthenticationRequired(false)
                .build();

        String unencryptedLongPassword = new Decryptor.DecryptorBuilder()
                .setEncryptedLongPassword(wallet.getEncryptedLongPassword())
                .setIV(wallet.getIV())
                .setWalletName(wallet.getFileName())
                .build()
                .decryptWalletPassword();

        assertNotNull(unencryptedLongPassword);
    }

    @Test(expected = UserNotAuthenticatedException.class)
    public void decryptWalletPassword_AuthenticationRequired_UserNotAuthenticatedException() throws Exception {
        Wallet wallet = new Wallet.WalletBuilder()
                .nickname("somewallet")
                .context(appContext)
                .build();

        String unencryptedLongPassword = new Decryptor.DecryptorBuilder()
                .setEncryptedLongPassword(wallet.getEncryptedLongPassword())
                .setIV(wallet.getIV())
                .setWalletName(wallet.getFileName())
                .build()
                .decryptWalletPassword();

        assertNotNull(unencryptedLongPassword);
    }

}