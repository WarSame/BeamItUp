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

        Encryption.Encryptor encryptor = encryptPrivateKey(ethAddress, privateKeyString);

        String decryptedText = decryptPrivateKey(ethAddress, encryptor.getIv(), encryptor.getEncryption());
        Toast.makeText(this, decryptedText, Toast.LENGTH_LONG).show();

        Eth eth = new Eth(ethAddress, encryptor.getEncryption());

        Account account = (Account) getIntent().getSerializableExtra("account");
        account.addEthereumAccount(eth);
        //Attach eth in DB

        onCreateEthSuccess(account);
    }

    private String decryptPrivateKey(String ethAddress, byte[] encryption, byte[] iv) {
        Encryption.Decryptor decryptor = null;
        String decryptedText = null;
        try {
            decryptor = new Encryption.Decryptor();
            decryptedText = decryptor.decryptText(ethAddress, encryption, iv);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedText;
    }

    private Encryption.Encryptor encryptPrivateKey(String ethAddress, String privateKeyString) {
        Encryption.Encryptor encryptor = new Encryption.Encryptor();
        try {
            encryptor.encryptText(ethAddress, privateKeyString);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "failed", Toast.LENGTH_LONG).show();
            onCreateEthFail();
        }
        return encryptor;
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
