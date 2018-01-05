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

        prepareReadyTransferMessage();
    }

    private void prepareReadyTransferMessage() {
        String amount = getIntent().getStringExtra("amount");
        TextView tv_amount_value = (TextView)findViewById(R.id.tv_amount_value);
        tv_amount_value.setText(amount);

        String reason = getIntent().getStringExtra("reason");
        TextView tv_amount_reason = (TextView)findViewById(R.id.tv_reason_value);
        tv_amount_reason.setText(reason);

        String senderPublicKey = getIntent().getStringExtra("senderAddress");

        Transfer tran = new Transfer(amount, reason, senderPublicKey);

        Log.i(TAG, "Sending transfer with amount: " + amount
                + " reason: " + reason
                + " senderAddress: " + senderPublicKey);

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
