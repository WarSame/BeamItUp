package com.example.graeme.beamitup;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EthDbAdapterTest {
    private EthDbAdapter ethDB;
    private Eth insertedEth;
    private Eth insertedEthWithID;

    @Before
    public void setUp() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        ethDB = new EthDbAdapter(appContext);

        DbAdapter.DatabaseHelper dbHelper = new DbAdapter.DatabaseHelper(appContext);
        dbHelper.onUpgrade(ethDB.db, 0, 1);//Wipe db tables

        insertedEth = new Eth("insertedaddress", 15);
        insertedEthWithID = new Eth("insertedaddresswithid", 2, 12);
    }

    @After
    public void tearDown() throws Exception {
        ethDB.close();
    }

    @Test
    public void createEth_InsertedAccountCreated_ShouldBeZero() throws Exception {
        long ethID = ethDB.createEth(insertedEth, "insertedprivatekey");
        System.out.println("ethid"+ethID);
        assertTrue(ethID == 1);
    }

    @Test
    public void createEth_InsertedAccountCreatedWithEthID_ShouldBeOne() throws Exception {
        ethDB.createEth(insertedEthWithID, "idinsertedprivatekey");
        System.out.println("ethidwithid"+insertedEthWithID.getId());
        assertTrue(insertedEthWithID.getId() == 2);
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