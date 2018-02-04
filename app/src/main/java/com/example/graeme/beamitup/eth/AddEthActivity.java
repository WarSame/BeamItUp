package com.example.graeme.beamitup.eth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.graeme.beamitup.R;
import com.example.graeme.beamitup.Session;
import com.example.graeme.beamitup.transfer.LandingPageActivity;
import com.example.graeme.beamitup.wallet.WalletHelper;

public class AddEthActivity extends Activity {
    private static final String TAG = "AddEthActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_eth);

        Button btn_add_eth = (Button) findViewById(R.id.btn_add_eth);

        btn_add_eth.setOnClickListener(
                (v) -> createEth((Button)v)
        );
    }

    private void createEth(Button btn_add_eth){
        btn_add_eth.setEnabled(false);
        EditText et_eth_nickname = (EditText) findViewById(R.id.et_eth_nickname);
        String nickname = et_eth_nickname.getText().toString();

        try {
            Eth eth = WalletHelper.generateWallet(
                    this,
                    nickname,
                    Session.getUserDetails().getId()
            );
            Log.i(TAG, "Wallet name: " + eth.getWalletName());
            Log.i(TAG, "Wallet address: " + eth.getAddress());
            Session.getUserDetails().addEth(eth);
            onCreateEthSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            onCreateEthFail();
        }
    }

    private void onCreateEthSuccess(){
        Button btn_add_eth = (Button) findViewById(R.id.btn_add_eth);
        btn_add_eth.setEnabled(true);

        Toast.makeText(this, "Eth created.", Toast.LENGTH_LONG).show();

        final Intent landingPageIntent = new Intent(this, LandingPageActivity.class);
        startActivity(landingPageIntent);
    }

    private void onCreateEthFail(){
        Button btn_add_eth = (Button) findViewById(R.id.btn_add_eth);
        btn_add_eth.setEnabled(true);

        Toast.makeText(this, "Eth creation failed.", Toast.LENGTH_LONG).show();
    }

}
