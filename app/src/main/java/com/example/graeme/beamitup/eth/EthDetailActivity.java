package com.example.graeme.beamitup.eth;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graeme.beamitup.BeamItUp;
import com.example.graeme.beamitup.R;

public class EthDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eth_detail);

        TextView tv_eth_wallet_name = findViewById(R.id.tv_eth_wallet_name);
        EditText et_eth_nickname = findViewById(R.id.et_eth_nickname);
        TextView tv_eth_address = findViewById(R.id.tv_eth_address);

        Eth eth = (Eth) getIntent().getSerializableExtra("eth");

        tv_eth_wallet_name.setText(eth.getWalletName());
        tv_eth_address.setText(eth.getAddress());
        et_eth_nickname.setText(eth.getNickname());

        Button btn_save_eth = findViewById(R.id.btn_save_eth);
        btn_save_eth.setOnClickListener((v)->{
            eth.setNickname(et_eth_nickname.getText().toString());
            updateEth(eth);
        });
    }

    private void updateEth(Eth eth) {
        DaoSession daoSession = ((BeamItUp) getApplication()).getDaoSession();
        EthDao ethDao = daoSession.getEthDao();
        ethDao.update(eth);
        Toast.makeText(this, "Eth saved", Toast.LENGTH_LONG).show();

        Intent ethListIntent = new Intent(this, EthListActivity.class);
        startActivity(ethListIntent);
    }

}
