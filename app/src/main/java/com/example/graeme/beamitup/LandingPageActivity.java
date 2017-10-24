package com.example.graeme.beamitup;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;

public class LandingPageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        Button btn_transfer_money = (Button)findViewById(R.id.btn_transfer_money);
        Button btn_transfer_history = (Button)findViewById(R.id.btn_transfer_history);

        final Intent transferMoneyIntent = new Intent(this, CreateTransferActivity.class);
        //final Intent transferHistoryIntent = new Intent();

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
