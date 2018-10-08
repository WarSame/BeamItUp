package com.example.graeme.beamitup.wallet;

import android.app.Fragment;
import android.app.KeyguardManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.graeme.beamitup.AddressListener;
import com.example.graeme.beamitup.AuthenticatorFragment;
import com.example.graeme.beamitup.BeamItUp;
import com.example.graeme.beamitup.CopyableAddressFragment;
import com.example.graeme.beamitup.LandingPageActivity;
import com.example.graeme.beamitup.R;
import com.example.graeme.beamitup.qr.CopyableQRImageFragment;

public class WalletDetailActivity extends Activity {
    private static final String TAG = "WalletDetailActivity";
    private Wallet wallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_detail);

        EditText et_wallet_nickname = findViewById(R.id.et_wallet_nickname);

        wallet = (Wallet) getIntent().getSerializableExtra("wallet");
        et_wallet_nickname.setText(wallet.getNickname());

        KeyguardManager kgm = (KeyguardManager) getApplication().getSystemService(Context.KEYGUARD_SERVICE);
        AuthenticatorFragment authenticatorFragment = new AuthenticatorFragment.AuthenticatorFragmentBuilder()
                .KGM(kgm)
                .onUserAuthenticatedListener(onUserAuthenticatedListener)
                .build();

        if (savedInstanceState == null) {
            getFragmentManager()
                    .beginTransaction()
                    .add(authenticatorFragment, "AuthenticatorFragment")
                    .commit();

            CopyableQRImageFragment qrFrag = (CopyableQRImageFragment) getFragmentManager()
                    .findFragmentById(R.id.frag_qr_code_display);
            qrFrag.setAddress(wallet.getAddress());
            CopyableAddressFragment addressFrag = (CopyableAddressFragment) getFragmentManager()
                    .findFragmentById(R.id.frag_copyable_text_display);
            addressFrag.setAddress(wallet.getAddress());
        }

        Button btn_save_wallet = findViewById(R.id.btn_save_wallet);

        btn_save_wallet.setOnClickListener((v)->{
            wallet.setNickname(et_wallet_nickname.getText().toString());
            updateWallet(wallet);
        });

        Button btn_export_wallet = findViewById(R.id.btn_export_wallet);

        btn_export_wallet.setOnClickListener((v)-> authenticatorFragment.authenticateMobileUser());
    }

    AuthenticatorFragment.OnUserAuthenticatedListener onUserAuthenticatedListener =
            new AuthenticatorFragment.OnUserAuthenticatedListener() {
        @Override
        public void onUserAuthenticated() {
            Log.i(TAG, "onUserAuthenticated");
            try {
                Log.i(TAG, "Copying secret key to clipboard");
                String privateKey = wallet.retrieveCredentials().getEcKeyPair().getPrivateKey().toString();
                ClipboardManager clipboard = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                copy(clipboard, "Private key of " + wallet.getNickname(), privateKey);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUserNotAuthenticated() {
            Log.i(TAG,"onUserNotAuthenticated");
        }
    };

    private void copy(ClipboardManager clipboard, String label, String data){
        ClipData clip = ClipData.newPlainText(label, data);
        if (clipboard == null || clip == null){
            return;
        }
        clipboard.setPrimaryClip(clip);
    }

    private void updateWallet(Wallet wallet) {
        DaoSession daoSession = ((BeamItUp) getApplication()).getDaoSession();
        WalletDao walletDao = daoSession.getWalletDao();
        walletDao.update(wallet);
        Toast.makeText(this, "Wallet saved", Toast.LENGTH_LONG).show();

        Intent walletListIntent = new Intent(this, LandingPageActivity.class);
        startActivity(walletListIntent);
    }
}
