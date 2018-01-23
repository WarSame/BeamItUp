package com.example.graeme.beamitup.request;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;

import com.example.graeme.beamitup.R;

public class FinishRequestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_request);

        Intent incomingIntent = getIntent();
        Request request = (Request)incomingIntent.getSerializableExtra("request");
    }

}
