package com.example.graeme.beamitup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LandingPageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        Button btn_transfer_money = (Button)findViewById(R.id.btn_transfer_money);
        Button btn_add_eth = (Button)findViewById(R.id.btn_add_eth);

        final Intent createTransferIntent = new Intent(this, CreateTransferActivity.class);
        final Intent addEthIntent = new Intent(this, AddEthActivity.class);

        btn_transfer_money.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(createTransferIntent);
            }
        });

        btn_add_eth.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(addEthIntent);
            }
        });
    }

}
