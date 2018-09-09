package com.example.graeme.beamitup.wallet;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.graeme.beamitup.AuthenticatorFragment;
import com.example.graeme.beamitup.R;

public class AddWalletActivity extends Activity {
    private static final String TAG = "AddWalletActivity";
    private boolean bound = false;
    private GenerateWalletService service = null;

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

        Log.i(TAG, "Binding service");
        Intent generateWalletIntent = new Intent(this, GenerateWalletService.class);
        this.bindService(generateWalletIntent, connection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        Log.i(TAG, "Unbinding service");
        unbindService(connection);
        bound = false;
    }

    AuthenticatorFragment.OnUserAuthenticatedListener onUserAuthenticatedListener = new AuthenticatorFragment.OnUserAuthenticatedListener() {
        @Override
        public void onUserAuthenticated() {
            Log.i(TAG, "onUserAuthenticated");
            EditText et_wallet_nickname = findViewById(R.id.et_wallet_nickname);
            String nickname = et_wallet_nickname.getText().toString();

            try {
                if (bound) {
                    Toast.makeText(getApplicationContext(), "Creating wallet", Toast.LENGTH_LONG).show();
                    Log.i(TAG, "Creating wallet");
                    service.generateWallet(nickname);

                    Intent walletListIntent = new Intent(getApplicationContext(), WalletListActivity.class);
                    startActivity(walletListIntent);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void onUserNotAuthenticated() {
            Log.i(TAG, "onUserNotAuthenticated");
        }
    };

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            GenerateWalletService.GenerateWalletBinder generateWalletBinder = (GenerateWalletService.GenerateWalletBinder) iBinder;
            service = generateWalletBinder.getService();
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            bound = false;
        }
    };
}
