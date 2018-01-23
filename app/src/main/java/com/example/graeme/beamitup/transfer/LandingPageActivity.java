package com.example.graeme.beamitup.transfer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.graeme.beamitup.eth.AddEthActivity;
import com.example.graeme.beamitup.R;

public class LandingPageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        Button btn_transfer_money = (Button)findViewById(R.id.btn_transfer_money);
        Button btn_add_eth = (Button)findViewById(R.id.btn_add_eth);

        final Intent createTransferIntent = new Intent(this, CreateTransferActivity.class);
        final Intent addEthIntent = new Intent(this, AddEthActivity.class);

        btn_transfer_money.setOnClickListener(v -> startActivity(createTransferIntent));

        btn_add_eth.setOnClickListener(v -> startActivity(addEthIntent));
    }

}
