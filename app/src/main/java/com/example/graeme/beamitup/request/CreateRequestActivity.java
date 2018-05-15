package com.example.graeme.beamitup.request;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graeme.beamitup.R;
import com.example.graeme.beamitup.eth.Eth;
import com.example.graeme.beamitup.eth.EthPickerFragment.onEthSelectedListener;
import com.example.graeme.beamitup.eth_tasks.GasPriceFragment;

public class CreateRequestActivity extends Activity implements onEthSelectedListener {
    Eth eth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request);

        Button btnCreateRequest = findViewById(R.id.btn_create_request);

        GasPriceFragment gasPriceFragment = new GasPriceFragment();

        TextView tv_gas_price_value = findViewById(R.id.tv_gas_cost_value);
        tv_gas_price_value.setText(gasPriceFragment.getTransactionGasCost());

        getFragmentManager()
                .beginTransaction()
                .add(gasPriceFragment, "GasPriceFragment")
                .commit();

        btnCreateRequest.setOnClickListener((v)->{
            EditText etAmount = findViewById(R.id.et_amount_value);
            String amount = etAmount.getText().toString();

            if (!isValidRequest(amount)){
                return;
            }

            readyRequestMessage(eth, amount);
        });
    }

    private void readyRequestMessage(Eth eth, String amount) {
        Request request = new Request(eth.getAddress(), amount);
        Intent readyRequestIntent = new Intent(this, ReadyRequestActivity.class);
        readyRequestIntent.putExtra("request", request);
        startActivity(readyRequestIntent);
    }

    @Override
    public void onEthSelected(Eth eth) {
        this.eth = eth;
    }

    private boolean isValidRequest(String amount){
        return isValidAmount(amount) && isValidEth();
    }

    private boolean isValidAmount(String amount){
        boolean isValidAmount = !amount.equals("") && Float.valueOf(amount) > 0;
        if (!isValidAmount) {
            Toast.makeText(this, "Invalid amount.", Toast.LENGTH_LONG).show();
        }
        return isValidAmount;
    }

    private boolean isValidEth(){
        boolean isValidEth = eth != null;
        if (!isValidEth) {
            Toast.makeText(this, "Invalid eth.", Toast.LENGTH_LONG).show();
        }
        return isValidEth;
    }
}
