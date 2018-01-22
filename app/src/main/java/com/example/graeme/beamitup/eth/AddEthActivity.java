package com.example.graeme.beamitup.eth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.graeme.beamitup.account.Account;
import com.example.graeme.beamitup.R;
import com.example.graeme.beamitup.Session;
import com.example.graeme.beamitup.transfer.LandingPageActivity;

public class AddEthActivity extends Activity {
    private static final String TAG = "AddEthActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_eth);

        Button btn_add_eth = (Button) findViewById(R.id.btn_add_eth);

        btn_add_eth.setOnClickListener(
                (v) -> createEth()
        );
    }

    private void createEth(){
        Button btn_add_eth = (Button) findViewById(R.id.btn_add_eth);
        btn_add_eth.setEnabled(false);

        EditText et_eth_address = (EditText) findViewById(R.id.et_eth_address);
        EditText et_private_key = (EditText) findViewById(R.id.et_private_key);

        String ethAddress = et_eth_address.getText().toString();
        String privateKey = et_private_key.getText().toString();

        createEthAndAddToSessionAccount(ethAddress, privateKey);

        onCreateEthSuccess();
    }

    private void createEthAndAddToSessionAccount(String ethAddress, String privateKey) {
        Account sessionAccount = Session.getUserDetails();
        Eth eth = createEthFromSessionAccountID(sessionAccount.getId(), ethAddress);
        long ethID = insertEthInDB(eth, privateKey);
        eth.setId(ethID);
        sessionAccount.addEth(eth);
    }

    private Eth createEthFromSessionAccountID(long sessionAccountID, String ethAddress) {
        Log.i(TAG, "Session account id: " + sessionAccountID);
        return new Eth(ethAddress, sessionAccountID);
    }

    private long insertEthInDB(Eth eth, String privateKey){
        EthDbAdapter db = new EthDbAdapter(this);
        long ethID = db.createEth(eth, privateKey);
        Log.i(TAG, "Setting eth id to " + ethID);
        db.close();
        return ethID;
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
