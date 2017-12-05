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
        String privateKeyString = et_private_key.getText().toString();

        Encryption.Encryptor encryptor = new Encryption.Encryptor();
        try {
            encryptor.encryptPrivateKey(ethAddress, privateKeyString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String decryptedText = null;
        try {
            decryptedText = new Encryption.Decryptor().decryptPrivateKey(ethAddress, encryptor.getEncryption(), encryptor.getIv());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Toast.makeText(this, decryptedText, Toast.LENGTH_LONG).show();

        Account account = (Account) getIntent().getSerializableExtra("account");

        Eth eth = new Eth(ethAddress, encryptor.getEncryption());
        eth.setAccountId(account.getId());

        EthDbAdapter ethDb = new EthDbAdapter(this);
        eth.setId(ethDb.createEth(eth));
        ethDb.close();

        account.addEthereumAccount(this, eth);

        onCreateEthSuccess(account);
    }


    private void onCreateEthSuccess(Account account){
        Button btn_add_eth = (Button) findViewById(R.id.btn_add_eth);
        btn_add_eth.setEnabled(true);

        final Intent landingPageIntent = new Intent(this, LandingPageActivity.class);
        landingPageIntent.putExtra("account", account);
        startActivity(landingPageIntent);
    }

    private void onCreateEthFail(){
        Button btn_add_eth = (Button) findViewById(R.id.btn_add_eth);
        btn_add_eth.setEnabled(true);

        Toast.makeText(this, "Ethereum account creation failed.", Toast.LENGTH_LONG).show();
    }

}
