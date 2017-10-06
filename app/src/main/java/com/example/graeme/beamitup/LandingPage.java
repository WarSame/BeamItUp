package com.example.graeme.beamitup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static com.example.graeme.beamitup.R.id.btn_ready_transfer;
import static com.example.graeme.beamitup.R.id.start;

public class LandingPage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        Button btn_transfer_money = (Button)findViewById(R.id.btn_transfer_money);
        Button btn_transfer_history = (Button)findViewById(R.id.btn_transfer_history);

        final Intent transferMoneyIntent = new Intent(this, CreateTransfer.class);
        Intent transferHistoryIntent = new Intent();

        btn_transfer_money.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(transferMoneyIntent);
            }
        });

        btn_transfer_history.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
            }
        });
    }
}
