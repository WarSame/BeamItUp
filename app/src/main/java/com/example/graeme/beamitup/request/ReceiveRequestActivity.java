package com.example.graeme.beamitup.request;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graeme.beamitup.LandingPageActivity;
import com.example.graeme.beamitup.R;
import com.example.graeme.beamitup.Session;
import com.example.graeme.beamitup.account.LoginActivity;
import com.example.graeme.beamitup.eth.Eth;
import com.example.graeme.beamitup.eth.EthPickerFragment;

import static com.example.graeme.beamitup.ndef.NdefMessaging.handlePushMessage;

public class ReceiveRequestActivity extends Activity implements EthPickerFragment.onEthSelectedListener{
    private static final String TAG = "ReceiveRequestActivity";
    static final int LOGIN_REQUEST = 0;
    Eth eth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!Session.isAlive()){
            Log.i(TAG, "Session is dead when receiving request");
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivityForResult(loginIntent, LOGIN_REQUEST);
        }
        else {
            handleRequest();
        }
    }

    private void handleRequest() {
        setContentView(R.layout.activity_receive_request);

        Request request = handlePushMessage(getIntent());

        TextView tvAmount = (TextView)findViewById(R.id.tv_amount_value);
        TextView tvToAddress = (TextView)findViewById(R.id.tv_to_address_value);

        if (request != null){
            tvAmount.setText(request.getAmount());
            tvToAddress.setText(request.getToAddress());
        }

        Button btnDeclineRequest = (Button)findViewById(R.id.btn_decline_request);
        Button btnAcceptRequest = (Button)findViewById(R.id.btn_accept_request);

        btnDeclineRequest.setOnClickListener(v->{
            Intent landingPageIntent = new Intent(this, LandingPageActivity.class);
            startActivity(landingPageIntent);
        });
        btnAcceptRequest.setOnClickListener(v->{
            v.setEnabled(false);
            if (request == null){
                Toast.makeText(this, "Error receiving message.", Toast.LENGTH_LONG).show();
                v.setEnabled(true);
                return;
            }
            if (eth == null){
                Toast.makeText(this, "You must select an eth account.", Toast.LENGTH_LONG).show();
                v.setEnabled(true);
                return;
            }
            Intent finishRequestIntent = new Intent(this, FinishRequestActivity.class);
            request.setFromAddress(eth.getAddress());
            finishRequestIntent.putExtra("request", request);

            v.setEnabled(true);
            startActivity(finishRequestIntent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == LOGIN_REQUEST){
            switch (resultCode){
                case RESULT_OK:
                    Log.i(TAG, "User logged in.");
                    handleRequest();
                    break;
                case RESULT_CANCELED:
                    //Just send them to the log in
                    Log.i(TAG, "User did not log in.");
                    Intent loginIntent = new Intent(this, LoginActivity.class);
                    startActivity(loginIntent);
                    break;
            }
        }
    }

    @Override
    public void onEthSelected(Eth eth) {
        this.eth = eth;
    }
}
