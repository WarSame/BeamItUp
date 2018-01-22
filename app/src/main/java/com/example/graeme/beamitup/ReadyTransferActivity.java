package com.example.graeme.beamitup;

import android.app.Activity;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.SerializationUtils;

public class ReadyTransferActivity extends Activity {
    private static final String TAG = "ReadyTransferActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready_transfer);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        String amount = getIntent().getStringExtra("amount");
        String senderAddress = getIntent().getStringExtra("senderAddress");

        TextView tv_amount_value = (TextView)findViewById(R.id.tv_amount_value);
        tv_amount_value.setText(amount);

        TextView tv_sender_address = (TextView)findViewById(R.id.tv_sender_address_value);
        tv_sender_address.setText(senderAddress);

        Transfer tran = new Transfer(amount, senderAddress);

        prepareReadyTransferMessage(tran);
    }

    private void prepareReadyTransferMessage(Transfer tran) {
        Log.i(TAG, "Sending transfer with amount: " + tran.getAmount()
                + " senderAddress: " + tran.getSenderAddress());

        NfcAdapter mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter == null) {
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        NdefRecord rec = NdefRecord.createMime(
                "application/" + getApplicationContext().getPackageName() + "/ready_transfer",
                SerializationUtils.serialize(tran)
        );
        NdefMessage msg = new NdefMessage(rec);

        mNfcAdapter.setNdefPushMessage(msg, this);
    }

}
