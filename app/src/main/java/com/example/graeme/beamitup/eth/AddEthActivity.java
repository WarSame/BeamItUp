package com.example.graeme.beamitup.eth;

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
import com.example.graeme.beamitup.eth_tasks.GenerateWalletService;

public class AddEthActivity extends Activity {
    private static final String TAG = "AddEthActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_eth);

        Button btn_add_eth = findViewById(R.id.btn_add_eth);

        KeyguardManager kgm = (KeyguardManager) getApplication().getSystemService(Context.KEYGUARD_SERVICE);
        AuthenticatorFragment authenticatorFragment = new AuthenticatorFragment.AuthenticatorFragmentBuilder()
                .KGM(kgm)
                .onUserAuthenticatedListener(onUserAuthenticatedListener)
                .build();

        getFragmentManager()
                .beginTransaction()
                .add(authenticatorFragment, "AuthenticatorFragment")
                .commit();

        btn_add_eth.setOnClickListener(
                (View v) -> authenticatorFragment.authenticateMobileUser()
        );
    }

    AuthenticatorFragment.OnUserAuthenticatedListener onUserAuthenticatedListener = new AuthenticatorFragment.OnUserAuthenticatedListener() {
        @Override
        public void onUserAuthenticated() {
            EditText et_eth_nickname = findViewById(R.id.et_eth_nickname);
            String nickname = et_eth_nickname.getText().toString();

            generateWallet(nickname);

            Toast.makeText(getApplicationContext(), "Creating eth", Toast.LENGTH_LONG).show();
            Log.i(TAG, "Creating eth");

            Intent ethListIntent = new Intent(getApplicationContext(), EthListActivity.class);
            startActivity(ethListIntent);
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
