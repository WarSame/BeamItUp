package com.example.graeme.beamitup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.graeme.beamitup.eth.AddEthActivity;
import com.example.graeme.beamitup.request.CreateRequestActivity;

public class LandingPageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        Session.createSession();

        Button btn_add_eth = (Button)findViewById(R.id.btn_add_eth);
        Button btn_create_request = (Button)findViewById(R.id.btn_create_request);

        final Intent addEthIntent = new Intent(this, AddEthActivity.class);
        final Intent createRequestIntent = new Intent(this, CreateRequestActivity.class);

        btn_add_eth.setOnClickListener(v -> startActivity(addEthIntent));

        btn_create_request.setOnClickListener(v-> startActivity(createRequestIntent));
    }

}
