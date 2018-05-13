package com.example.graeme.beamitup.eth;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.graeme.beamitup.Authenticator;
import com.example.graeme.beamitup.BeamItUp;
import com.example.graeme.beamitup.Encryption;
import com.example.graeme.beamitup.LandingPageActivity;
import com.example.graeme.beamitup.R;
import com.example.graeme.beamitup.eth_tasks.GenerateWalletTask;
import com.example.graeme.beamitup.wallet.EncryptedWallet;
import com.example.graeme.beamitup.wallet.WalletHelper;

import org.web3j.crypto.Credentials;

import java.io.File;

public class AddEthActivity extends Activity {
    private static final String TAG = "AddEthActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_eth);

        Button btn_add_eth = findViewById(R.id.btn_add_eth);

        btn_add_eth.setOnClickListener(
                (v) -> {
                    Authenticator authenticator = new Authenticator();
                    authenticator.setOnUserAuthenticatedListener(this::createEth);
                    authenticator.authenticateMobileUser();
                }
        );
    }

    private void createEth(int resultCode){
        switch (resultCode){
            case RESULT_OK:
                Toast.makeText(this, "User authenticated", Toast.LENGTH_LONG).show();
                Log.i(TAG, "User is authenticated");

                ProgressBar pbSendTransfer = findViewById(R.id.pb_create_wallet);
                pbSendTransfer.setVisibility(View.VISIBLE);

                EditText et_eth_nickname = findViewById(R.id.et_eth_nickname);
                String nickname = et_eth_nickname.getText().toString();

                generateWallet(nickname);

                enableAddEthButton();
                break;
            case RESULT_CANCELED:
                Toast.makeText(this, "User failed to authenticate", Toast.LENGTH_LONG).show();
                Log.i(TAG, "User is not authenticated");

                enableAddEthButton();
                break;
        }
    }

    private void generateWallet(String nickname){
        try {
            String longPassword = Encryption.generateLongRandomString();
            File walletDir = WalletHelper.getWalletDir(this);
            GenerateWalletTask generateWalletTask = new GenerateWalletTask(
                    longPassword,
                    walletDir,
                    (String walletName)-> {
                        if (walletName == null){
                            Log.i(TAG, "Failed to create wallet");
                            Toast.makeText(this, "Failed to create eth", Toast.LENGTH_LONG).show();
                        }
                        else {
                            handleWalletCreation(walletName, nickname, longPassword);
                        }
                    }
            );
            generateWalletTask.execute();
        } catch (Exception e) {
            e.printStackTrace();
            onCreateEthFail();
        }
        finally {
            enableAddEthButton();
        }
    }

    private void handleWalletCreation(String walletName, String nickname, String longPassword) throws Exception{
        File walletFile = WalletHelper.getWalletFile(this, walletName);
        Credentials credentials = WalletHelper.retrieveCredentials(walletFile, longPassword);

        EncryptedWallet encryptedWallet = Encryption.encryptWalletPassword(walletName, longPassword);

        Eth eth = new Eth();
        eth.setNickname(nickname);
        eth.setAddress(credentials.getAddress());
        eth.setWalletName(walletName);
        eth.setEncryptedLongPassword(encryptedWallet.getEncryptedLongPassword());
        eth.setIV(encryptedWallet.getIV());

        insertEth(eth);

        removeProgressBar();

        onCreateEthSuccess();
    }

    private void enableAddEthButton() {
        Button btn_add_eth = findViewById(R.id.btn_add_eth);
        btn_add_eth.setEnabled(true);
    }

    private void insertEth(Eth eth){
        DaoSession daoSession = ((BeamItUp)getApplication()).getDaoSession();
        EthDao ethDao = daoSession.getEthDao();
        ethDao.insert(eth);
        Log.d(TAG, "Inserted new eth " + eth.getId());
    }

    private void removeProgressBar(){
        ProgressBar pbSendTransfer = findViewById(R.id.pb_create_wallet);
        pbSendTransfer.setVisibility(View.GONE);
    }

    private void onCreateEthSuccess(){
        Button btn_add_eth = findViewById(R.id.btn_add_eth);
        btn_add_eth.setEnabled(true);

        Toast.makeText(this, "Eth created.", Toast.LENGTH_LONG).show();

        final Intent landingPageIntent = new Intent(this, LandingPageActivity.class);
        startActivity(landingPageIntent);
    }

    private void onCreateEthFail(){
        Button btn_add_eth = findViewById(R.id.btn_add_eth);
        btn_add_eth.setEnabled(true);

        Toast.makeText(this, "Eth creation failed.", Toast.LENGTH_LONG).show();
    }

}
