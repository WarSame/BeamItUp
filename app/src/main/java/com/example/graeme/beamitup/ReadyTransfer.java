package com.example.graeme.beamitup;

import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class ReadyTransfer extends Activity implements NfcAdapter.CreateNdefMessageCallback {

    NfcAdapter mNfcAdapter;
    Transfer tran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready_transfer);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        String amount = getIntent().getStringExtra("amount");
        TextView tv_amount_value = (TextView)findViewById(R.id.tv_amount_value);
        tv_amount_value.setText(amount);

        String reason = getIntent().getStringExtra("reason");
        TextView tv_amount_reason = (TextView)findViewById(R.id.tv_reason_value);
        tv_amount_reason.setText(reason);

        tran = new Transfer(amount, reason);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null){
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        mNfcAdapter.setNdefPushMessageCallback(this, ReadyTransfer.this);
    }

    public NdefMessage createNdefMessage(NfcEvent event) {
        Toast.makeText(this, "landed", Toast.LENGTH_LONG).show();
        try {
            Toast.makeText(this, tran.toString(), Toast.LENGTH_LONG).show();
            return new NdefMessage(tran.toString().getBytes());
        } catch (FormatException e) {
            Toast.makeText(this, "format exception", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return null;
    }
}
