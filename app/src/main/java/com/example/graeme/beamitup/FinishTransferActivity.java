package com.example.graeme.beamitup;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.util.ArrayList;
import java.util.concurrent.Future;

public class FinishTransferActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_transfer);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Transfer tran = getReplyTransferMessage();

        Account account = (Account) getIntent().getSerializableExtra("account");
        ArrayList<Eth> ethIds = account.getEths();

        TextView tv_transfer_status = (TextView)findViewById(R.id.tv_finish_transfer_status_value);

        try {
            String senderPrivateKey = "";
            tran.setSenderPrivateKey(senderPrivateKey);
            Future<TransactionReceipt> future = tran.send(this);
            future.get();
            tv_transfer_status.setText( getResources().getString(R.string.transfer_succeeded) );
        } catch (Exception e) {
            e.printStackTrace();
            tv_transfer_status.setText( getResources().getString(R.string.transfer_failed) );
        }

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
