package com.example.graeme.beamitup;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.app.Activity;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ReplyTransferActivity extends Activity {
    Account account;
    int lv_position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_transfer);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        final ListView lv_select_account = (ListView)findViewById(R.id.lv_transfer_money_account);

        account = (Account) getIntent().getSerializableExtra("account");
        ArrayList<Eth> eths = account.getEths();

        EthAdapter ethAdapter = new EthAdapter(this, eths);
        lv_select_account.setAdapter(ethAdapter);

        lv_select_account.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
                lv_position = position;
            }
        });

        Eth eth = (Eth)lv_select_account.getItemAtPosition(lv_position);
        Transfer tran = getReadyTransferMessage();
        prepareReplyTransfer(tran, eth);
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

    void prepareReplyTransfer(Transfer tran, Eth eth){
        NfcAdapter mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        tran.setReceiverPublicKey(eth.getAddress());

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
