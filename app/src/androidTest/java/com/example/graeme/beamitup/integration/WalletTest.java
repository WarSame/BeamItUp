package com.example.graeme.beamitup.integration;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import com.example.graeme.beamitup.wallet.Wallet;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

public class WalletTest {
    private static final String TAG = "WalletTest";

    private static Context appContext;

    @BeforeClass
    public static void setUpOneTime(){
        appContext = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void createWallet_ShouldBeWallet() throws Exception{
        String nickname = "some nickname";
        Wallet wallet = new Wallet.WalletBuilder()
                .nickname(nickname)
                .context(appContext)
                .isUserAuthenticationRequired(false)
                .build();

        Log.d(TAG, "Wallet nickname: " + wallet.getNickname());
        assertEquals(wallet.getNickname(), nickname);

        Log.d(TAG, "Wallet address: " + wallet.getAddress());
        assertNotNull(wallet.getAddress());

        Log.d(TAG, "Wallet name: " + wallet.getFileName());
        assertNotNull(wallet.getFileName());

        Log.d(TAG, "Wallet location: " + wallet.getLocation());
        assertNotNull(wallet.getLocation());
        assertTrue(wallet.getLocation().contains(Wallet.WALLET_DIR_RELATIVE_PATH));

        Log.d(TAG, "Wallet encrypted long password: " + Arrays.toString(wallet.getEncryptedLongPassword()));
        assertNotNull(wallet.getEncryptedLongPassword());

        Log.d(TAG, "Wallet IV: " + Arrays.toString(wallet.getIV()));
        assertNotNull(wallet.getIV());
    }

}
