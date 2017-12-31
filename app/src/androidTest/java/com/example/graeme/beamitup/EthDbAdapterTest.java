package com.example.graeme.beamitup;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EthDbAdapterTest {
    private EthDbAdapter ethDB;

    @Before
    public void setUp() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        ethDB = new EthDbAdapter(appContext);

        DbAdapter.DatabaseHelper dbHelper = new DbAdapter.DatabaseHelper(appContext);
        dbHelper.onUpgrade(ethDB.db, 0, 1);//Wipe db tables
    }

    @After
    public void tearDown() throws Exception {
        ethDB.close();
    }

    @Test
    public void createEth() throws Exception {
    }

    @Test
    public void retrieveEthByEthID() throws Exception {
    }

    @Test
    public void retrieveEthsByAccountId() throws Exception {
    }

    @Test
    public void updateEths() throws Exception {
    }

    @Test
    public void deleteEth() throws Exception {
    }

}