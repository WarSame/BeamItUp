package com.example.graeme.beamitup.eth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.graeme.beamitup.Encryption;
import com.example.graeme.beamitup.LandingPageActivity;
import com.example.graeme.beamitup.R;
import com.example.graeme.beamitup.Session;
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

        Button btn_add_eth = (Button) findViewById(R.id.btn_add_eth);

        btn_add_eth.setOnClickListener(
                (v) -> createEth((Button)v)
        );
    }

    private void createEth(Button btn_add_eth){
        ProgressBar pbSendTransfer = (ProgressBar)findViewById(R.id.pb_create_wallet);
        pbSendTransfer.setVisibility(View.VISIBLE);

        btn_add_eth.setEnabled(false);
        EditText et_eth_nickname = (EditText) findViewById(R.id.et_eth_nickname);
        String nickname = et_eth_nickname.getText().toString();

        try {
            String longPassword = Encryption.generateLongRandomString();
            File walletDir = WalletHelper.getWalletDir(this);
            GenerateWalletTask generateWalletTask = new GenerateWalletTask(
                    longPassword,
                    walletDir,
                    (String walletName)-> {
                        if (walletName == null){
                            Log.i(TAG, "Failed to create wallet");
                        }
                        else {
                            Log.i(TAG, "Created wallet " + walletName);

                            EthDbAdapter ethDbAdapter = new EthDbAdapter(this);
                            File walletFile = WalletHelper.getWalletFile(this, walletName);
                            WalletHelper.insertWalletEth(walletName, longPassword, nickname, ethDbAdapter, walletFile);
                            ethDbAdapter.close();

                            removeProgressBar();

                            onCreateEthSuccess();
                        }
                    }
            );
            generateWalletTask.execute();
        } catch (Exception e) {
            e.printStackTrace();
            onCreateEthFail();
        }
    }

    private void removeProgressBar(){
        ProgressBar pbSendTransfer = (ProgressBar)findViewById(R.id.pb_create_wallet);
        pbSendTransfer.setVisibility(View.GONE);
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
