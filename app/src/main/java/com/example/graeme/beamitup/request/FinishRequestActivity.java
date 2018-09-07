package com.example.graeme.beamitup.request;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.example.graeme.beamitup.R;
import com.example.graeme.beamitup.transaction.SendTransactionTask;

public class FinishRequestActivity extends Activity {
    private static final String TAG = "FinishRequestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_request);

        Intent incomingIntent = getIntent();
        Request request = (Request)incomingIntent.getSerializableExtra("request");
        try {
            sendTransaction(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendTransaction(Request request){
        Intent sendTransactionIntent = new Intent(this, SendTransactionTask.class);
        sendTransactionIntent.putExtra("request", request);
        startService(sendTransactionIntent);
    }
}
