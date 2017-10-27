package com.example.graeme.beamitup;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.app.Activity;
import android.widget.Toast;

public class FinishTransferActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_transfer);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Transfer tran = getReplyTransferMessage();

        //Perform ethereum transfer now that we have all the necessary information

    }

    Transfer getReplyTransferMessage(){
        Transfer tran = null;
        Intent intent = getIntent();
        if (intent.getType() != null && intent.getType().equals("application/" + getPackageName() + "/reply_transfer")){
            NdefMessage msg = (NdefMessage)intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)[0];
            tran = Transfer.fromBytes(msg.getRecords()[0].getPayload());
            Toast.makeText(this, tran.toString(), Toast.LENGTH_LONG).show();
        }
        return tran;
    }

}
