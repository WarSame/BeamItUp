package com.example.graeme.beamitup;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ServiceTestRule;
import android.util.Log;

import com.example.graeme.beamitup.wallet.GenerateWalletService;
import com.example.graeme.beamitup.wallet.GenerateWalletService.GenerateWalletBinder;
import com.example.graeme.beamitup.wallet.Wallet;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;

public class GenerateWalletServiceTest {
    private static final String TAG = "GenerateWalletServiceTest";
    private static Context appContext;
    @Rule
    public final ServiceTestRule serviceTestRule = new ServiceTestRule();

    @BeforeClass
    public static void setupOneTime(){
        appContext = InstrumentationRegistry.getTargetContext();
    }

    private GenerateWalletService service;
    private boolean bound = false;

    @Test
    public void generateWallet_ShouldBeWallet() throws Exception {
        Intent intent = new Intent(appContext, GenerateWalletService.class);
        ServiceConnection connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                GenerateWalletBinder generateWalletBinder = (GenerateWalletBinder) iBinder;
                service = generateWalletBinder.getService();
                bound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                bound = false;
            }
        };
        if (!bound) {
            serviceTestRule.bindService(intent, connection, Context.BIND_AUTO_CREATE);
        }
        Log.d(TAG, service.toString());
        service.setIsUserAuthenticationRequired(false);
        Wallet wallet = service.generateWallet("somenickname");
        assertTrue(wallet != null);
    }
}
