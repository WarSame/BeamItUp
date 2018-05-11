package com.example.graeme.beamitup.eth;

import android.os.Bundle;
import android.app.Activity;
import android.widget.EditText;
import android.widget.TextView;

import com.example.graeme.beamitup.R;

public class EthDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eth_detail);

        TextView tv_eth_wallet_name = findViewById(R.id.tv_eth_wallet_name);
        EditText et_eth_nickname = findViewById(R.id.et_eth_nickname);
        EditText et_eth_address = findViewById(R.id.et_eth_address);

        Eth eth = (Eth) getIntent().getSerializableExtra("eth");
        tv_eth_wallet_name.setText(eth.getWalletName());
        et_eth_address.setText(eth.getAddress());
        et_eth_nickname.setText(eth.getNickname());
    }

}
