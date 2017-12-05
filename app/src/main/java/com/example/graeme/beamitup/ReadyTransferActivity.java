package com.example.graeme.beamitup;

import android.app.Activity;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class ReadyTransferActivity extends Activity {
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
        NfcAdapter mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter == null) {
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        NdefRecord rec = NdefRecord.createMime("application/" + getApplicationContext().getPackageName() + "/ready_transfer",
                Transfer.toBytes(tran));
        NdefMessage msg = new NdefMessage(rec);

        Toast.makeText(this, Transfer.fromBytes(msg.getRecords()[0].getPayload()).toString(), Toast.LENGTH_LONG).show();

        mNfcAdapter.setNdefPushMessage(msg, this);
    }

}
