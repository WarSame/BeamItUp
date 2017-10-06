package com.example.graeme.beamitup;

import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.app.Activity;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateTransfer extends Activity{
    Intent readyTransferIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_money);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        readyTransferIntent = new Intent(this, ReadyTransfer.class);

        final Button btn_ready_transfer = (Button) findViewById(R.id.btn_ready_transfer);
        btn_ready_transfer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                readyTransferIntent.putExtra("amount", ((EditText)findViewById(R.id.edittext_transfer_money_amount)).getText().toString());
                readyTransferIntent.putExtra("reason", ((EditText)findViewById(R.id.edittext_transfer_money_reason)).getText().toString());
                startActivity(readyTransferIntent);
            }
        });
    }

}
