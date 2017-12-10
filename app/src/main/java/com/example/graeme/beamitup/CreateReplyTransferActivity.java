package com.example.graeme.beamitup;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CreateReplyTransferActivity extends FragmentActivity {

    Intent replyTransferIntent;
    Transfer tran;
    Eth eth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_reply_transfer);

        tran = getReadyTransferMessage();
        displayTransferDetails(tran);

        Button btn_ready_reply = (Button)findViewById(R.id.btn_ready_reply);

        replyTransferIntent = new Intent(this, ReplyTransferActivity.class);
        btn_ready_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replyTransferIntent.putExtra("transfer", tran);
                replyTransferIntent.putExtra("eth", eth);//Care, mnight be null
                startActivity(replyTransferIntent);
            }
        });
    }

    void displayTransferDetails(Transfer tran){
        TextView tv_transfer_money_reason = (TextView)findViewById(R.id.tv_transfer_money_reason);
        TextView tv_transfer_money_amount = (TextView)findViewById(R.id.tv_transfer_money_amount);
        tv_transfer_money_amount.setText(tran.getAmount());
        tv_transfer_money_reason.setText(tran.getReason());
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


}
