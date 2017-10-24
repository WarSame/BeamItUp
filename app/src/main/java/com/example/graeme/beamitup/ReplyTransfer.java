package com.example.graeme.beamitup;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.app.Activity;
import android.provider.Settings;
import android.widget.Toast;

public class ReplyTransfer extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_transfer);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Transfer tran = getReadyTransferMessage();
        prepareReplyTransfer(tran);
    }

    Transfer getReadyTransferMessage(){
        Transfer tran = null;
        Intent intent = getIntent();
        if (intent.getType() != null && intent.getType().equals("application/" + getPackageName() + "/ready_transfer")){
            NdefMessage msg = (NdefMessage)intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)[0];
            tran = Transfer.fromBytes(msg.getRecords()[0].getPayload());
            Toast.makeText(this, tran.toString(), Toast.LENGTH_LONG).show();
        }
        return tran;
    }

    void prepareReplyTransfer(Transfer tran){
        NfcAdapter mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        tran.setReceiverPublicKey(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));

        if (mNfcAdapter == null) {
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        NdefRecord rec = NdefRecord.createMime("application/" + getApplicationContext().getPackageName() + "/reply_transfer",
                Transfer.toBytes(tran));
        NdefMessage msg = new NdefMessage(rec);

        Toast.makeText(this, Transfer.fromBytes(msg.getRecords()[0].getPayload()).toString(), Toast.LENGTH_LONG).show();

        mNfcAdapter.setNdefPushMessage(msg, this);
    }

}
