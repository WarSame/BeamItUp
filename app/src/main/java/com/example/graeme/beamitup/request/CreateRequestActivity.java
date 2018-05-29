package com.example.graeme.beamitup.request;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graeme.beamitup.BeamItUp;
import com.example.graeme.beamitup.R;
import com.example.graeme.beamitup.wallet.Wallet;
import com.example.graeme.beamitup.wallet.WalletPickerFragment.onWalletSelectedListener;
import com.example.graeme.beamitup.GasPriceTask;

import org.web3j.protocol.Web3j;

public class CreateRequestActivity extends Activity implements onWalletSelectedListener {
    Wallet wallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request);

        Button btnCreateRequest = findViewById(R.id.btn_create_request);

        btnCreateRequest.setOnClickListener((v)->{
            EditText etAmount = findViewById(R.id.et_amount_value);
            String amount = etAmount.getText().toString();

            if (!isValidRequest(amount)){
                return;
            }

            readyRequestMessage(wallet, amount);
        });

        Web3j web3j = ((BeamItUp)getApplication()).getWeb3j();
        GasPriceTask.DetermineGasPriceResponse gasPriceResponse = (String gasCost) ->{
            TextView tvGasCost = findViewById(R.id.tv_gas_cost_value);
            tvGasCost.setText(gasCost);
        };
        new GasPriceTask(gasPriceResponse, web3j).execute();
    }

    private void readyRequestMessage(Wallet wallet, String amount) {
        Request request = new Request(wallet.getAddress(), amount);
        Intent readyRequestIntent = new Intent(this, ReadyRequestActivity.class);
        readyRequestIntent.putExtra("request", request);
        startActivity(readyRequestIntent);
    }

    @Override
    public void onWalletSelected(Wallet wallet) {
        this.wallet = wallet;
    }

    private boolean isValidRequest(String amount){
        return isValidAmount(amount) && isValidWallet();
    }

    private boolean isValidAmount(String amount){
        boolean isValidAmount = !amount.equals("") && Float.valueOf(amount) > 0;
        if (!isValidAmount) {
            Toast.makeText(this, "Invalid amount.", Toast.LENGTH_LONG).show();
        }
        return isValidAmount;
    }

    private boolean isValidWallet(){
        boolean isValidWallet = wallet != null;
        if (!isValidWallet) {
            Toast.makeText(this, "Invalid wallet.", Toast.LENGTH_LONG).show();
        }
        return isValidWallet;
    }
}
