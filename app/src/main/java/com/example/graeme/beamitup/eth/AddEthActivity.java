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

import org.web3j.crypto.Credentials;

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
        EditText et_eth_password = (EditText) findViewById(R.id.et_eth_password);

        String nickname = et_eth_nickname.getText().toString();
        String password = et_eth_password.getText().toString();

        try {
            String walletName = WalletHelper.generateWallet(this, password);
            Log.i(TAG, "Walletname: " + walletName);
            Credentials credentials = WalletHelper.retrieveCredentials(this, password, walletName);
            Log.i(TAG, "Wallet address: " + credentials.getAddress());
            createEthAndAddToSessionAccount(nickname, walletName, credentials.getAddress(), password);
            onCreateEthSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            onCreateEthFail();
        }
    }

    private void createEthAndAddToSessionAccount(String nickname, String walletName, String address, String password) {
        Account sessionAccount = Session.getUserDetails();
        Eth eth = createEthFromSessionAccountID(sessionAccount.getId(), nickname, address, walletName);
        long ethID = insertEthInDB(eth, password);
        eth.setId(ethID);
        sessionAccount.addEth(eth);
    }

    private Eth createEthFromSessionAccountID(long sessionAccountID, String nickname, String address, String walletName) {
        Log.i(TAG, "Session account id: " + sessionAccountID);
        return new Eth(nickname, walletName, address, sessionAccountID);
    }

    private long insertEthInDB(Eth eth, String password){
        EthDbAdapter db = new EthDbAdapter(this);
        long ethID = db.createEth(eth, password);
        Log.i(TAG, "Setting eth id to " + ethID);
        db.close();
        return ethID;
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
