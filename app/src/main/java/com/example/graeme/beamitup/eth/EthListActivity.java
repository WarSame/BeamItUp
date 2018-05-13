package com.example.graeme.beamitup.eth;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.Button;

import com.example.graeme.beamitup.R;

public class EthListActivity extends Activity implements EthPickerFragment.onEthSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eth_list);
        Button btn_add_eth = findViewById(R.id.btn_add_eth);
        btn_add_eth.setOnClickListener((v)->{
            Intent addEthIntent = new Intent(this, AddEthActivity.class);
            startActivity(addEthIntent);
        });
    }

    @Override
    public void onEthSelected(Eth eth) {
        Intent ethDetail = new Intent(this, EthDetailActivity.class);
        ethDetail.putExtra("eth", eth);
        startActivity(ethDetail);
    }
}
