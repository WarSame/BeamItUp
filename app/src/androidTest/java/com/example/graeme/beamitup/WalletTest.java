package com.example.graeme.beamitup;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import com.example.graeme.beamitup.wallet.Wallet;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;

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
        assertTrue(wallet.getNickname().equals(nickname));

        Log.d(TAG, "Wallet address: " + wallet.getAddress());
        assertTrue(wallet.getAddress() != null);

        Log.d(TAG, "Wallet name: " + wallet.getFileName());
        assertTrue(wallet.getFileName() != null);

        Log.d(TAG, "Wallet location: " + wallet.getLocation());
        assertTrue(wallet.getLocation() != null);
        assertTrue(wallet.getLocation().contains(Wallet.WALLET_DIR_RELATIVE_PATH));

        Log.d(TAG, "Wallet encrypted long password: " + Arrays.toString(wallet.getEncryptedLongPassword()));
        assertTrue(wallet.getEncryptedLongPassword() != null);

        Log.d(TAG, "Wallet IV: " + Arrays.toString(wallet.getIV()));
        assertTrue(wallet.getIV() != null);
    }

}
