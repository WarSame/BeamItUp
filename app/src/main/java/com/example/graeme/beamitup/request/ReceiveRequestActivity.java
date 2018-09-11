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
import com.example.graeme.beamitup.wallet.Wallet;
import com.example.graeme.beamitup.wallet.WalletPickerFragment;

import static com.example.graeme.beamitup.ndef.NdefMessaging.handlePushMessage;

public class ReceiveRequestActivity extends Activity implements WalletPickerFragment.onWalletSelectedListener{
    private static final String TAG = "ReceiveRequestActivity";
    private Wallet wallet;
    private Request request;
    private SendTransactionService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handleRequest();
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
            try {
                request.setFromCredentials(wallet.retrieveCredentials());
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error sending transaction", Toast.LENGTH_LONG).show();
                return;
            }
            sendTransaction();
            Intent landingPageIntent = new Intent(getApplicationContext(), LandingPageActivity.class);

            enableAcceptRequestButton();
            startActivity(landingPageIntent);
        }

        @Override
        public void onUserNotAuthenticated() {
            enableAcceptRequestButton();
        }
    };

    private void sendTransaction(){
        Intent sendTransactionIntent = new Intent(this, SendTransactionService.class);
        ServiceConnection connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                SendTransactionBinder sendTransactionBinder = (SendTransactionBinder) iBinder;
                service = sendTransactionBinder.getService();
                try {
                    Log.d(TAG, "Sending transaction");
                    service.sendTransaction(request, (receipt)->{
                    });
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Sending the transaction", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
            }
        };
        this.bindService(sendTransactionIntent, connection, BIND_AUTO_CREATE);
    }

    private void enableAcceptRequestButton(){
        Button btnAcceptRequest = findViewById(R.id.btn_accept_request);
        btnAcceptRequest.setEnabled(true);
    }

    @Override
    public void onWalletSelected(Wallet wallet) {
        this.wallet = wallet;
    }
}
