package com.example.graeme.beamitup.transfer;

import android.app.Activity;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graeme.beamitup.R;
import com.example.graeme.beamitup.eth.Eth;

import org.apache.commons.lang3.SerializationUtils;

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
        eth = (Eth)getIntent().getSerializableExtra("eth");

        prepareReplyTransfer(tran, eth);
        displayTransferDetails();
    }

    private void displayTransferDetails() {
        TextView tv_reply_transfer_amount = (TextView)findViewById(R.id.tv_transfer_money_amount);
        TextView tv_reply_transfer_receiver_address = (TextView)findViewById(R.id.tv_transfer_money_receiver_address);
        tv_reply_transfer_amount.setText(tran.getAmount());
        tv_reply_transfer_receiver_address.setText(tran.getToAddress());
    }

    void prepareReplyTransfer(Transfer tran, Eth eth){
        NfcAdapter mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        tran.setToAddress(eth.getAddress());

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

        Log.i(TAG, "Eth reply selected id: " + eth.getId() + " address: " + eth.getAddress());

        mNfcAdapter.setNdefPushMessage(msg, this);
    }

}
