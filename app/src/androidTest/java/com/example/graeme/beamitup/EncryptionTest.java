package com.example.graeme.beamitup;

import com.example.graeme.beamitup.wallet.EncryptedWallet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EncryptionTest {
    private static final String TAG = "EncryptionTest";

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void hashPassword() throws Exception {
    }

    @Test
    public void generateSalt() throws Exception {
    }

    @Test
    public void storeWalletFile() throws Exception {
        String longPassword = Encryption.generateLongRandomString();
        EncryptedWallet encryptedWallet = Encryption.encryptWalletPassword("somewallet", longPassword);
    }

}