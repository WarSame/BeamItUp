package com.example.graeme.beamitup.request;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.graeme.beamitup.R;
import com.example.graeme.beamitup.ndef.NdefMessaging;

public class ReadyRequestActivity extends Activity {
    private static final String TAG = "ReadyRequestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready_request);

        Intent incomingIntent = getIntent();
        Request request = (Request)incomingIntent.getSerializableExtra("request");
        prepareRequestMessage(request);
        fillViewValues(request);
    }

    private void fillViewValues(Request request) {
        TextView tvToAddress = (TextView)findViewById(R.id.tv_to_address_value);
        TextView tvAmount = (TextView)findViewById(R.id.tv_amount_value);

        tvToAddress.setText(request.getToAddress());
        tvAmount.setText(request.getAmount());
    }

    private void prepareRequestMessage(Request request){
        Log.i(TAG, "Preparing request message of " + request.toString());
        NdefMessaging.preparePushMessage(request, this, getPackageName() + "/request");
    }


}
