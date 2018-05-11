package com.example.graeme.beamitup.eth;

import android.os.Bundle;
import android.app.Activity;

import com.example.graeme.beamitup.R;

public class EthDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eth_detail);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
