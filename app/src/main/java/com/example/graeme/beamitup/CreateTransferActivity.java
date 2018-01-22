package com.example.graeme.beamitup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


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

        btn_ready_transfer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                btn_ready_transfer.setEnabled(false);
                boolean isValidAmount = isValidAmount();
                if (eth == null){
                    Log.d(TAG, "No ethereum account selected.");
                    Toast.makeText(
                            getApplicationContext(),
                            "You must select a valid ethereum account.",
                            Toast.LENGTH_LONG
                    ).show();
                    enableReadyButton();
                }
                else if (!isValidAmount){
                    Log.d(TAG, "No valid amount selected.");
                    Toast.makeText(
                            getApplicationContext(),
                            "You must select a valid amount.",
                            Toast.LENGTH_LONG
                    ).show();
                    enableReadyButton();
                }
                else {
                    onCreateTransferSuccess();
                }
            }
        });
    }

    boolean isValidAmount(){
        String amount = ((EditText)findViewById(R.id.et_transfer_money_amount)).getText().toString();
        return !amount.equals("") && Integer.valueOf(amount) > 0;
    }

    public void onEthSelected(Eth eth){
        Log.d(TAG, "Eth selected. ID: " + eth.getId() + " Address: " + eth.getAddress());
        this.eth = eth;
    }

    private void onCreateTransferSuccess(){
        enableReadyButton();

        String amount = ((EditText)findViewById(R.id.et_transfer_money_amount)).getText().toString();

        readyTransferIntent.putExtra("senderAddress", eth.getAddress());
        readyTransferIntent.putExtra("amount", amount);
        startActivity(readyTransferIntent);
    }

    void enableReadyButton(){
        Button btn_ready_transfer = (Button) findViewById(R.id.btn_ready_transfer);
        btn_ready_transfer.setEnabled(true);
    }

}
