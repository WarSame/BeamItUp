package com.example.graeme.beamitup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddEthActivity extends Activity {
    private static final String TAG = "AddEthActivity";

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

        Account sessionAccount = addEthToSessionAccount(ethAddress);
        Eth eth = new Eth(ethAddress, sessionAccount.getId());
        associateEthWithAccountInDB(eth, privateKey);
        onCreateEthSuccess();
    }

    private Account addEthToSessionAccount(String ethAddress) {
        Account account = Session.getUserDetails();
        Eth eth = new Eth(ethAddress, account.getId());

        account.addEth(eth);
        return account;
    }

    private void associateEthWithAccountInDB(Eth eth, String privateKey){
        EthDbAdapter db = new EthDbAdapter(this);
        long ethID = db.createEth(eth, privateKey);
        Log.i(TAG, "Setting eth id to " + ethID);
        eth.setId(ethID);
        db.close();
    }


    private void onCreateEthSuccess(){
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
