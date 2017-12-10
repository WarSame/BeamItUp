package com.example.graeme.beamitup;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class CreateTransferActivity extends Activity{
    private static final String TAG = "CreateTransferActivity";
    Intent readyTransferIntent;
    Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_transfer);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        readyTransferIntent = new Intent(this, ReadyTransferActivity.class);
        final Button btn_ready_transfer = (Button) findViewById(R.id.btn_ready_transfer);

        account = Session.getUserDetails();

        btn_ready_transfer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                btn_ready_transfer.setEnabled(false);
                boolean isEthSelected = isEthSelected();
                boolean isValidAmount = isValidAmount();
                if ( isEthSelected && isValidAmount ){
                    onCreateTransferSuccess();
                    return;
                }
                if ( !isEthSelected ){
                    alertEthNotSelected();
                }
                if ( !isValidAmount ){
                    alertInvalidAmount();
                }
                onCreateTransferFail();
            }
        });
    }

    boolean isEthSelected(){
        //return lv_position != -1;
        return true;
    }

    boolean isValidAmount(){
        String amount = ((EditText)findViewById(R.id.et_transfer_money_amount)).getText().toString();
        return !amount.equals("") && Integer.valueOf(amount) > 0;
    }

    void alertEthNotSelected(){
        Log.d(TAG, "No ethereum account selected.");
        Toast.makeText(
                getApplicationContext(),
                "You must select a valid ethereum account.",
                Toast.LENGTH_LONG
        ).show();
    }

    void alertInvalidAmount(){
        Log.d(TAG, "No valid amount selected.");
        Toast.makeText(
                getApplicationContext(),
                "You must select a valid amount.",
                Toast.LENGTH_LONG
        ).show();
    }

    private void onCreateTransferFail(){
        Button btn_ready_transfer = (Button) findViewById(R.id.btn_ready_transfer);
        btn_ready_transfer.setEnabled(true);
        Toast.makeText(this, "Transfer creation failed", Toast.LENGTH_LONG).show();
    }

    private void onCreateTransferSuccess(){
        String amount = ((EditText)findViewById(R.id.et_transfer_money_amount)).getText().toString();
        String reason = ((EditText)findViewById(R.id.et_transfer_money_reason)).getText().toString();

        //readyTransferIntent.putExtra("senderAddress", eth.getAddress());
        readyTransferIntent.putExtra("amount", amount);
        readyTransferIntent.putExtra("reason", reason);
        startActivity(readyTransferIntent);
    }

}
