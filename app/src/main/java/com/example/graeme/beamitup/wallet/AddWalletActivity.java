package com.example.graeme.beamitup.wallet;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.graeme.beamitup.AuthenticatorFragment;
import com.example.graeme.beamitup.R;

public class AddWalletActivity extends Activity {
    private static final String TAG = "AddWalletActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wallet);

        Button btn_add_wallet = findViewById(R.id.btn_add_wallet);

        KeyguardManager kgm = (KeyguardManager) getApplication().getSystemService(Context.KEYGUARD_SERVICE);
        AuthenticatorFragment authenticatorFragment = new AuthenticatorFragment.AuthenticatorFragmentBuilder()
                .KGM(kgm)
                .onUserAuthenticatedListener(onUserAuthenticatedListener)
                .build();

        getFragmentManager()
                .beginTransaction()
                .add(authenticatorFragment, "AuthenticatorFragment")
                .commit();

        btn_add_wallet.setOnClickListener(
                (View v) -> authenticatorFragment.authenticateMobileUser()
        );
    }

    AuthenticatorFragment.OnUserAuthenticatedListener onUserAuthenticatedListener = new AuthenticatorFragment.OnUserAuthenticatedListener() {
        @Override
        public void onUserAuthenticated() {
            EditText et_wallet_nickname = findViewById(R.id.et_wallet_nickname);
            String nickname = et_wallet_nickname.getText().toString();

            generateWallet(nickname);

            Toast.makeText(getApplicationContext(), "Creating wallet", Toast.LENGTH_LONG).show();
            Log.i(TAG, "Creating wallet");

            Intent walletListIntent = new Intent(getApplicationContext(), WalletListActivity.class);
            startActivity(walletListIntent);
        }

        @Override
        public void onUserNotAuthenticated() {
        }
    };

    private void generateWallet(String nickname){
        try {
            Intent generateWalletIntent = new Intent(this, GenerateWalletService.class)
                .putExtra("nickname", nickname);
            startService(generateWalletIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
