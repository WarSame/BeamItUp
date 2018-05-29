package com.example.graeme.beamitup.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.Button;

import com.example.graeme.beamitup.R;

public class WalletListActivity extends Activity implements WalletPickerFragment.onWalletSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_list);
        Button btn_add_wallet = findViewById(R.id.btn_add_wallet);
        btn_add_wallet.setOnClickListener((v)->{
            Intent addWalletIntent = new Intent(this, AddWalletActivity.class);
            startActivity(addWalletIntent);
        });
    }

    @Override
    public void onWalletSelected(Wallet wallet) {
        Intent walletDetail = new Intent(this, WalletDetailActivity.class);
        walletDetail.putExtra("wallet", wallet);
        startActivity(walletDetail);
    }
}
