package com.example.graeme.beamitup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.utils.Convert;
import com.example.graeme.beamitup.DetermineGasPriceTask.*;

import java.math.BigDecimal;
import java.math.BigInteger;

public class CreateTransferActivity extends FragmentActivity
    implements EthPickerFragment.onEthSelectedListener
{
    private static final String TAG = "CreateTransferActivity";
    Intent readyTransferIntent;
    Eth eth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_transfer);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        readyTransferIntent = new Intent(this, ReadyTransferActivity.class);
        final Button btn_ready_transfer = (Button) findViewById(R.id.btn_ready_transfer);

        btn_ready_transfer.setOnClickListener(
                (v) -> {
                    Button btnReadyTransfer = (Button)v;
                    btnReadyTransfer.setEnabled(false);
                    boolean isValidAmount = isValidAmount();
                    if (eth == null) {
                        Log.d(TAG, "No ethereum account selected.");
                        Toast.makeText(
                                getApplicationContext(),
                                "You must select a valid ethereum account.",
                                Toast.LENGTH_LONG
                        ).show();
                        enableReadyButton(btnReadyTransfer);
                    } else if (!isValidAmount) {
                        Log.d(TAG, "No valid amount selected.");
                        Toast.makeText(
                                getApplicationContext(),
                                "You must select a valid amount.",
                                Toast.LENGTH_LONG
                        ).show();
                        enableReadyButton(btnReadyTransfer);
                    } else {
                        onCreateTransferSuccess(btnReadyTransfer);
                    }
                });

        try {
            getCurrentGasCostAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getCurrentGasCostAsync() throws Exception {
        DetermineGasPriceResponse determineGasPriceResponse = this::updateGasCost;

        new DetermineGasPriceTask(determineGasPriceResponse).execute();
    }

    private void updateGasCost(EthGasPrice ethGasPrice){
        TextView tvGasCost = (TextView)findViewById(R.id.tv_gas_cost_value);
        try {
            String gasCost = getCurrentGasCost( ethGasPrice.getGasPrice() );
            tvGasCost.setText(gasCost);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getCurrentGasCost(BigInteger currentGasPrice){
        BigInteger GAS_USED = org.web3j.tx.Transfer.GAS_LIMIT;

        BigInteger gasCost = GAS_USED.multiply(currentGasPrice);
        return Convert.fromWei(new BigDecimal(gasCost), Convert.Unit.ETHER ).toString();
    }

    boolean isValidAmount(){
        String amount = ((EditText)findViewById(R.id.et_transfer_money_amount)).getText().toString();
        return !amount.equals("") && Float.valueOf(amount) > 0;
    }

    public void onEthSelected(Eth eth){
        Log.d(TAG, "Eth selected. ID: " + eth.getId() + " Address: " + eth.getAddress());
        this.eth = eth;
    }

    private void onCreateTransferSuccess(Button btnReadyTransfer){
        enableReadyButton(btnReadyTransfer);

        String amount = ((EditText)findViewById(R.id.et_transfer_money_amount)).getText().toString();

        readyTransferIntent.putExtra("senderAddress", eth.getAddress());
        readyTransferIntent.putExtra("amount", amount);
        startActivity(readyTransferIntent);
    }

    void enableReadyButton(Button btnReadyTransfer){
        btnReadyTransfer.setEnabled(true);
    }

}
