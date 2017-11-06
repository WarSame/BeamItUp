package com.example.graeme.beamitup;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

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
        byte[] privateKeyEnc = new byte[0];
        try {
            privateKeyEnc = encryptor.encryptText(ethAddress, privateKeyString);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "failed", Toast.LENGTH_LONG).show();
            onCreateEthFail();
        }

        Encryption.Decryptor decryptor;
        try {
            decryptor = new Encryption.Decryptor();
            Toast.makeText(this, decryptor.decryptText(ethAddress, encryptor.getEncryption(), encryptor.getIv()), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Eth eth = new Eth(ethAddress, privateKeyEnc);

        Account account = (Account) getIntent().getSerializableExtra("account");
        account.addEthereumAccount(eth);
        //Attach eth in DB

        onCreateEthSuccess(account);
    }

    private void onCreateEthSuccess(Account account){
        Button btn_add_eth = (Button) findViewById(R.id.btn_add_eth);
        btn_add_eth.setEnabled(true);

        //Intent landingPageIntent = new Intent(this, LandingPageActivity.class);
        //landingPageIntent.putExtra("account", account);
        //startActivity(landingPageIntent);
    }

    private void onCreateEthFail(){
        Button btn_add_eth = (Button) findViewById(R.id.btn_add_eth);
        btn_add_eth.setEnabled(true);

        //Toast.makeText(this, "Ethereum account creation failed.", Toast.LENGTH_LONG).show();
    }

}
