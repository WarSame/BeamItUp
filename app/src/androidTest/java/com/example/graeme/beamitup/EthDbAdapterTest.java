package com.example.graeme.beamitup;

import android.content.Context;
import android.security.keystore.UserNotAuthenticatedException;
import android.support.test.InstrumentationRegistry;

import com.example.graeme.beamitup.eth.Eth;
import com.example.graeme.beamitup.eth.EthDbAdapter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertTrue;

public class EthDbAdapterTest {
    private static EthDbAdapter ethDB;

    @Before
    public void setUp() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        ethDB = new EthDbAdapter(appContext);

        DbAdapter.DatabaseHelper dbHelper = new DbAdapter.DatabaseHelper(appContext);
        dbHelper.onUpgrade(ethDB.db, 0, 1);//Wipe db tables
    }

    @AfterClass
    public static void tearDown(){
        ethDB.close();
    }

    @Test
    public void createEth_InsertedEthCreated_ShouldBeOne(){
        byte[] encryptedLongPassword = new byte[]{};
        byte[] IV = new byte[]{};
        Eth eth = new Eth(
                "somenick",
                "someaddress",
                "somewalletname",
                encryptedLongPassword,
                IV
        );
        long ethID = ethDB.createEth(eth);
        System.out.println("ethid " + ethID);
        assertTrue(1 == ethID);
    }

    @Test
    public void retrieveEthByEthID() {
    }

    @Test
    public void retrieveSenderPrivateKey() {
    }

    @Test
    public void updateEth() {
    }

    @Test
    public void deleteEth() {
    }

}