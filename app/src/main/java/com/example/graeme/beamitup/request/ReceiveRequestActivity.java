package com.example.graeme.beamitup.request;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graeme.beamitup.AuthenticatorFragment;
import com.example.graeme.beamitup.AuthenticatorFragment.OnUserAuthenticatedListener;
import com.example.graeme.beamitup.LandingPageActivity;
import com.example.graeme.beamitup.R;
import com.example.graeme.beamitup.transaction.SendTransactionService;
import com.example.graeme.beamitup.transaction.SendTransactionService.SendTransactionBinder;
import com.example.graeme.beamitup.transaction.Transaction;
import com.example.graeme.beamitup.wallet.GenerateWalletService;
import com.example.graeme.beamitup.wallet.Wallet;
import com.example.graeme.beamitup.wallet.WalletPickerFragment;

import static com.example.graeme.beamitup.ndef.NdefMessaging.handlePushMessage;

public class ReceiveRequestActivity extends Activity implements WalletPickerFragment.onWalletSelectedListener{
    private static final String TAG = "ReceiveRequestActivity";
    private Wallet wallet;
    private Request request;
    private SendTransactionService service;
    private boolean bound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "Binding service");
        Intent generateWalletIntent = new Intent(this, SendTransactionService.class);
        this.bindService(generateWalletIntent, connection, BIND_AUTO_CREATE);
        handleRequest();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        Log.i(TAG, "Unbinding service");
        unbindService(connection);
        bound = false;
    }

    private void handleRequest() {
        setContentView(R.layout.activity_receive_request);

        request = handlePushMessage(getIntent());

        TextView tvAmount = findViewById(R.id.tv_amount_value);
        TextView tvToAddress = findViewById(R.id.tv_to_address_value);

        if (request == null){
            Toast.makeText(this, "Error receiving message.", Toast.LENGTH_LONG).show();
            Intent landingPageIntent = new Intent(this, LandingPageActivity.class);
            startActivity(landingPageIntent);
        }
        else {
            tvAmount.setText(request.getAmount());
            tvToAddress.setText(request.getToAddress());
        }

        Button btnDeclineRequest = findViewById(R.id.btn_decline_request);
        Button btnAcceptRequest = findViewById(R.id.btn_accept_request);

        btnDeclineRequest.setOnClickListener(v->{
            Intent landingPageIntent = new Intent(this, LandingPageActivity.class);
            startActivity(landingPageIntent);
        });

        KeyguardManager kgm = (KeyguardManager) getApplication().getSystemService(Context.KEYGUARD_SERVICE);
        AuthenticatorFragment authenticatorFragment = new AuthenticatorFragment.AuthenticatorFragmentBuilder()
                .KGM(kgm)
                .onUserAuthenticatedListener(onUserAuthenticatedListener)
                .build();

        getFragmentManager()
                .beginTransaction()
                .add(authenticatorFragment, "AuthenticatorFragment")
                .commit();

        btnAcceptRequest.setOnClickListener(v->{
            v.setEnabled(false);
            if (wallet == null){
                Toast.makeText(this, "You must select an wallet account.", Toast.LENGTH_LONG).show();
                v.setEnabled(true);
                return;
            }
            authenticatorFragment.authenticateMobileUser();
        });
    }

    OnUserAuthenticatedListener onUserAuthenticatedListener = new OnUserAuthenticatedListener() {
        @Override
        public void onUserAuthenticated() {
            Log.d(TAG, "Sending transaction");
            Transaction transaction = new Transaction(wallet, request);
            service.sendTransaction(transaction,
                    (receipt)->
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Sending transaction",
                                    Toast.LENGTH_LONG
                            )
                            .show()
            );
            Intent landingPageIntent = new Intent(getApplicationContext(), LandingPageActivity.class);

            enableAcceptRequestButton();
            startActivity(landingPageIntent);
        }

        @Override
        public void onUserNotAuthenticated() {
            enableAcceptRequestButton();
        }
    };

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            SendTransactionBinder generateWalletBinder = (SendTransactionBinder) iBinder;
            service = generateWalletBinder.getService();
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            bound = false;
        }
    };

    private void enableAcceptRequestButton(){
        Button btnAcceptRequest = findViewById(R.id.btn_accept_request);
        btnAcceptRequest.setEnabled(true);
    }

    @Override
    public void onWalletSelected(Wallet wallet) {
        this.wallet = wallet;
    }
}
