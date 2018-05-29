package com.example.graeme.beamitup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.graeme.beamitup.wallet.WalletListActivity;
import com.example.graeme.beamitup.request.CreateRequestActivity;

public class LandingPageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        Button btn_wallet_list = findViewById(R.id.btn_wallet_list);
        Button btn_create_request = findViewById(R.id.btn_create_request);

        final Intent walletListIntent = new Intent(this, WalletListActivity.class);
        final Intent createRequestIntent = new Intent(this, CreateRequestActivity.class);

        btn_wallet_list.setOnClickListener(v -> startActivity(walletListIntent));

        btn_create_request.setOnClickListener(v-> startActivity(createRequestIntent));
    }

}
