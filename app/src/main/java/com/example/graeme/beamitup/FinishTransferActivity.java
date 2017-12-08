package com.example.graeme.beamitup;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.util.concurrent.Future;

public class FinishTransferActivity extends Activity {
    private static final String TAG = "FinishTransferActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_transfer);
        getActionBar().setDisplayHomeAsUpEnabled(true);


        if (!Session.isAlive()){
            final Intent loginIntent = new Intent(this, MainActivity.class);
            startActivity(loginIntent);
        }

        Transfer tran = getReplyTransferMessage();
        Account account = Session.getUserDetails();
        Eth eth = selectEthFromAccountByAddress(account, tran);

        TextView tv_transfer_status = (TextView)findViewById(R.id.tv_finish_transfer_status_value);

        try {
            String senderPrivateKey = getSenderPrivateKey(eth, tran);
            tran.setSenderPrivateKey(senderPrivateKey);
            Future<TransactionReceipt> future = tran.send(this);
            future.get();
            tv_transfer_status.setText( getResources().getString(R.string.transfer_succeeded) );
            Log.d(TAG, "Transfer succeeded.");
        } catch (Exception e) {
            e.printStackTrace();
            tv_transfer_status.setText( getResources().getString(R.string.transfer_failed) );
            Log.d(TAG, "Transfer failed.");
        }
    }

    Eth selectEthFromAccountByAddress(Account account, Transfer tran){
        String senderAddress = tran.getSenderAddress();
        for ( Eth eth : account.getEths() ){
            if ( eth.getAddress().equals( senderAddress ) ){
                return eth;
            }
        }
        return null;
    }

    String getSenderPrivateKey(Eth eth, Transfer tran) throws Exception{
        byte[] senderPrivateKeyEnc = eth.getEncPrivateKey();
        byte[] senderIv = eth.getIv();

        Encryption.Decryptor decryptor = new Encryption.Decryptor();
        return decryptor.decryptPrivateKey(
                tran.getSenderAddress(),
                senderPrivateKeyEnc,
                senderIv
        );
    }

    Transfer getReplyTransferMessage(){
        Transfer tran = null;
        Intent intent = getIntent();
        Boolean isReplyTransfer = intent.getType() != null && intent.getType().equals("application/" + getPackageName() + "/reply_transfer");
        if ( isReplyTransfer ){
            NdefMessage msg = (NdefMessage)intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)[0];
            tran = Transfer.fromBytes(msg.getRecords()[0].getPayload());
            Toast.makeText(this, tran.toString(), Toast.LENGTH_LONG).show();
        }
        return tran;
    }

}
