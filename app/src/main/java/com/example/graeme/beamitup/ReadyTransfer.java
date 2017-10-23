package com.example.graeme.beamitup;

import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.nio.charset.StandardCharsets;

public class ReadyTransfer extends Activity {
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

        Transfer tran = new Transfer(amount, reason);

        NfcAdapter mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        NdefRecord rec = NdefRecord.createExternal("com.example.graeme.beamitup", "transfer", tran.toString().getBytes());
        NdefMessage msg = new NdefMessage(rec);

        String str = new String(msg.getRecords()[0].getPayload(), StandardCharsets.UTF_8);
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();

        mNfcAdapter.setNdefPushMessage(msg, this);
    }

}
