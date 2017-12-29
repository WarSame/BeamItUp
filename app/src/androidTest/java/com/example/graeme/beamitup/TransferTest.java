package com.example.graeme.beamitup;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TransferTest {
    Context appContext;
    @Test
    public void useAppContext() throws Exception {
        appContext = InstrumentationRegistry.getTargetContext();
    }
}
