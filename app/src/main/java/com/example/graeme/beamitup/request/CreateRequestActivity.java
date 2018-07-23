package com.example.graeme.beamitup.request;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graeme.beamitup.BeamItUp;
import com.example.graeme.beamitup.R;
import com.example.graeme.beamitup.wallet.Wallet;
import com.example.graeme.beamitup.wallet.WalletPickerFragment;

import org.web3j.protocol.Web3j;
import rx.schedulers.Schedulers;
import com.example.graeme.beamitup.utils.AndroidSchedulers;

import static org.web3j.tx.Transfer.GAS_LIMIT;

public class CreateRequestActivity extends Activity implements WalletPickerFragment.onWalletSelectedListener {
    Wallet wallet;
    private static final String TAG = "CreateRequestActivity";

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
        web3j.ethGasPrice().observable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(price -> setGasPrice(price.getGasPrice().multiply(GAS_LIMIT).toString()),
                Throwable::printStackTrace);
    }

    private void setGasPrice(String gasPrice){
        Log.i(TAG, "Setting gas price to " + gasPrice);
        TextView tv_gas_cost = findViewById(R.id.tv_gas_cost_value);
        tv_gas_cost.setText(gasPrice);
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
