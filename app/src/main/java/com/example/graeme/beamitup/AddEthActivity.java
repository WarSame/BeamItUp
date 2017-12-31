package com.example.graeme.beamitup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddEthActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_eth);

        Button btn_add_eth = (Button) findViewById(R.id.btn_add_eth);

        btn_add_eth.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createEth();
                    }
                }
        );
    }

    private void createEth(){
        Button btn_add_eth = (Button) findViewById(R.id.btn_add_eth);
        btn_add_eth.setEnabled(false);

        EditText et_eth_address = (EditText) findViewById(R.id.et_eth_address);
        EditText et_private_key = (EditText) findViewById(R.id.et_private_key);

        String ethAddress = et_eth_address.getText().toString();
        String privateKey = et_private_key.getText().toString();

        Account account = Session.getUserDetails();
        addEthToSessionAccount(ethAddress, privateKey, account);
        onCreateEthSuccess(account);
    }

    private void addEthToSessionAccount(String ethAddress, String privateKey, Account account) {
        Eth eth = new Eth(ethAddress, account.getId());

        addEthToAccountInDB(account, eth, privateKey);
        account.addEth(eth);
    }

    private void addEthToAccountInDB(Account account, Eth eth, String privateKey){
        EthDbAdapter db = new EthDbAdapter(this);
        long ethID = db.createEth(eth, privateKey);
        eth.setId(ethID);
        db.close();
    }


    private void onCreateEthSuccess(Account account){
        Button btn_add_eth = (Button) findViewById(R.id.btn_add_eth);
        btn_add_eth.setEnabled(true);

        final Intent landingPageIntent = new Intent(this, LandingPageActivity.class);
        startActivity(landingPageIntent);
    }

    private void onCreateEthFail(){
        Button btn_add_eth = (Button) findViewById(R.id.btn_add_eth);
        btn_add_eth.setEnabled(true);

        Toast.makeText(this, "Ethereum account creation failed.", Toast.LENGTH_LONG).show();
    }

}
