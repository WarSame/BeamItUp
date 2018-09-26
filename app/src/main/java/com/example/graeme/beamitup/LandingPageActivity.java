package com.example.graeme.beamitup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.graeme.beamitup.wallet.AddWalletActivity;
import com.example.graeme.beamitup.wallet.Wallet;
import com.example.graeme.beamitup.wallet.WalletDetailActivity;
import com.example.graeme.beamitup.request.CreateRequestActivity;
import com.example.graeme.beamitup.wallet.WalletPickerFragment.onWalletSelectedListener;

public class LandingPageActivity extends Activity implements onWalletSelectedListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        Button btn_create_request = findViewById(R.id.btn_create_request);
        final Intent createRequestIntent = new Intent(this, CreateRequestActivity.class);
        btn_create_request.setOnClickListener(v-> startActivity(createRequestIntent));

        Button btn_add_wallet = findViewById(R.id.btn_add_wallet);
        final Intent addWalletIntent = new Intent(this, AddWalletActivity.class);
        btn_add_wallet.setOnClickListener((v)-> startActivity(addWalletIntent));
    }

    @Override
    public void onWalletSelected(Wallet wallet) {
        Intent walletDetail = new Intent(this, WalletDetailActivity.class);
        walletDetail.putExtra("wallet", wallet);
        startActivity(walletDetail);
    }
}
