package com.example.graeme.beamitup;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.example.graeme.beamitup.eth.Eth;
import com.example.graeme.beamitup.eth.EthDbAdapter;
import com.example.graeme.beamitup.wallet.WalletHelper;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class EthDbAdapterTest {
    private static Context appContext;
    private static EthDbAdapter ethDB;
    private static final long INSERTED_ACCOUNT_ID = 15;

    @Before
    public void setUp() throws Exception {
        appContext = InstrumentationRegistry.getTargetContext();
        ethDB = new EthDbAdapter(appContext);

        DbAdapter.DatabaseHelper dbHelper = new DbAdapter.DatabaseHelper(appContext);
        dbHelper.onUpgrade(ethDB.db, 0, 1);//Wipe db tables
    }

    @AfterClass
    public static void tearDown() throws Exception {
        ethDB.close();
    }

    @Test
    public void createEth_InsertedAccountCreated_ShouldBeOne() throws Exception {
        Eth insertedEth = WalletHelper.generateWallet(appContext, "inserted eth", INSERTED_ACCOUNT_ID);
        System.out.println("ethid " + insertedEth.getId());
        assertTrue(1 == insertedEth.getId());
    }

    @Test
    public void retrieveEthByEthID() throws Exception {
    }

    @Test
    public void retrieveEthsByAccountId() throws Exception {
    }

    @Test
    public void retrieveSenderPrivateKey() throws Exception {
    }

    @Test
    public void updateEth() throws Exception {
    }

    @Test
    public void deleteEth() throws Exception {
    }

}