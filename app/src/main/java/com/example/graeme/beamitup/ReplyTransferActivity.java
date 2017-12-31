package com.example.graeme.beamitup;

import android.app.Activity;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.SerializationUtils;

import java.util.Arrays;

public class ReplyTransferActivity extends Activity {
    private static final String TAG = "ReplyTransferActivity";
    Transfer tran;
    Eth eth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_transfer);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        tran = (Transfer)getIntent().getSerializableExtra("transfer");
        eth = getIntent().getParcelableExtra("eth");

        prepareReplyTransfer(tran, eth);
        displayTransferDetails();
    }

    private void displayTransferDetails() {
        TextView tv_reply_transfer_reason = (TextView)findViewById(R.id.tv_transfer_money_reason);
        TextView tv_reply_transfer_amount = (TextView)findViewById(R.id.tv_transfer_money_amount);
        TextView tv_reply_transfer_receiver_address = (TextView)findViewById(R.id.tv_transfer_money_receiver_address);
        tv_reply_transfer_amount.setText(tran.getAmount());
        tv_reply_transfer_reason.setText(tran.getReason());
        tv_reply_transfer_receiver_address.setText(tran.getReceiverAddress());
    }

    void prepareReplyTransfer(Transfer tran, Eth eth){
        NfcAdapter mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        tran.setReceiverAddress(eth.getAddress());

        if (mNfcAdapter == null) {
            Toast.makeText(
                    this,
                    "NFC is not available",
                    Toast.LENGTH_LONG
            ).show();
            finish();
            return;
        }

        NdefRecord rec = NdefRecord.createMime(
                "application/" + getApplicationContext().getPackageName() + "/reply_transfer",
                SerializationUtils.serialize(tran)
        );
        NdefMessage msg = new NdefMessage(rec);

        Toast.makeText(
                this,
                Arrays.toString(msg.getRecords()[0].getPayload()),
                Toast.LENGTH_LONG
        ).show();

        mNfcAdapter.setNdefPushMessage(msg, this);
    }

}
