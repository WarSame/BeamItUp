package com.example.graeme.beamitup;

import android.os.Bundle;
import android.app.Activity;

public class ReplyTransfer extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_transfer);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
