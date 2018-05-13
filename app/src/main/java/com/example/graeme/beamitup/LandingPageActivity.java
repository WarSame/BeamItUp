package com.example.graeme.beamitup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.graeme.beamitup.eth.AddEthActivity;
import com.example.graeme.beamitup.eth.EthListActivity;
import com.example.graeme.beamitup.request.CreateRequestActivity;

public class LandingPageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        Button btn_eth_list = findViewById(R.id.btn_eth_list);
        Button btn_create_request = findViewById(R.id.btn_create_request);

        final Intent ethListIntent = new Intent(this, EthListActivity.class);
        final Intent createRequestIntent = new Intent(this, CreateRequestActivity.class);

        btn_eth_list.setOnClickListener(v -> startActivity(ethListIntent));

        btn_create_request.setOnClickListener(v-> startActivity(createRequestIntent));
    }

}
