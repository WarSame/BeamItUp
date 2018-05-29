package com.example.graeme.beamitup.wallet;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.graeme.beamitup.AddressCopyableTextDisplayFragment;
import com.example.graeme.beamitup.BeamItUp;
import com.example.graeme.beamitup.AddressQRCodeDisplayFragment;
import com.example.graeme.beamitup.R;
import com.example.graeme.beamitup.wallet.DaoSession;
import com.example.graeme.beamitup.wallet.WalletDao;

public class WalletDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_detail);

        EditText et_wallet_nickname = findViewById(R.id.et_wallet_nickname);

        Wallet wallet = (Wallet) getIntent().getSerializableExtra("wallet");
        et_wallet_nickname.setText(wallet.getNickname());

        Fragment qrCodeDisplayFragment = AddressQRCodeDisplayFragment.newInstance(wallet.getAddress());
        Fragment addressCopyableTextDisplayFragment = AddressCopyableTextDisplayFragment.newInstance(wallet.getAddress());
        getFragmentManager()
                .beginTransaction()
                .add(qrCodeDisplayFragment, "AddressQRCodeDisplayFragment")
                .add(addressCopyableTextDisplayFragment, "AddressCopyableTextDisplayFragment")
                .commit();

        Button btn_save_wallet = findViewById(R.id.btn_save_wallet);

        btn_save_wallet.setOnClickListener((v)->{
            wallet.setNickname(et_wallet_nickname.getText().toString());
            updateWallet(wallet);
        });
    }

    private void updateWallet(Wallet wallet) {
        DaoSession daoSession = ((BeamItUp) getApplication()).getDaoSession();
        WalletDao walletDao = daoSession.getWalletDao();
        walletDao.update(wallet);
        Toast.makeText(this, "Wallet saved", Toast.LENGTH_LONG).show();

        Intent walletListIntent = new Intent(this, WalletListActivity.class);
        startActivity(walletListIntent);
    }

}
