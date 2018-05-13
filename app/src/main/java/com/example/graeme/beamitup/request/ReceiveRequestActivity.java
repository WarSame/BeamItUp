package com.example.graeme.beamitup.request;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graeme.beamitup.Authenticator;
import com.example.graeme.beamitup.LandingPageActivity;
import com.example.graeme.beamitup.R;
import com.example.graeme.beamitup.eth.Eth;
import com.example.graeme.beamitup.eth.EthPickerFragment;

import static com.example.graeme.beamitup.ndef.NdefMessaging.handlePushMessage;

public class ReceiveRequestActivity extends Activity implements EthPickerFragment.onEthSelectedListener{
    private static final String TAG = "ReceiveRequestActivity";
    Eth eth;
    Request request;

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

        btnAcceptRequest.setOnClickListener(v->{
            v.setEnabled(false);
            if (eth == null){
                Toast.makeText(this, "You must select an eth account.", Toast.LENGTH_LONG).show();
                v.setEnabled(true);
                return;
            }

            Authenticator authenticator = new Authenticator();
            authenticator.setOnUserAuthenticatedListener(this::handleMobileAuthenticateResponse);
            authenticator.authenticateMobileUser();
        });
    }

    private void handleMobileAuthenticateResponse(int resultCode) {
        switch (resultCode){
            case RESULT_OK:
                Toast.makeText(this, "User authenticated", Toast.LENGTH_LONG).show();
                Log.i(TAG, "User is authenticated");

                Intent finishRequestIntent = new Intent(this, FinishRequestActivity.class);
                request.setFromID(eth.getId());
                finishRequestIntent.putExtra("request", request);

                enableAcceptRequestButton();
                startActivity(finishRequestIntent);
                break;
            case RESULT_CANCELED:
                Toast.makeText(this, "User failed to authenticate", Toast.LENGTH_LONG).show();
                Log.i(TAG, "User is not authenticated");

                enableAcceptRequestButton();
                break;
        }
    }

    private void enableAcceptRequestButton(){
        Button btnAcceptRequest = findViewById(R.id.btn_accept_request);
        btnAcceptRequest.setEnabled(true);
    }

    @Override
    public void onEthSelected(Eth eth) {
        this.eth = eth;
    }
}
