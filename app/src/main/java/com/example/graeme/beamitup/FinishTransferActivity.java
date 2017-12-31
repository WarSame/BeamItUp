package com.example.graeme.beamitup;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.lang3.SerializationUtils;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.util.NoSuchElementException;
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
        try {
            Eth eth = selectEthFromAccountByAddress(account, tran);
            sendTransfer(eth, tran);
        }
        catch (Exception e){
            e.printStackTrace();
            String transferFailedText = "Transfer to " + tran.getReceiverAddress() + " failed.";
            Toast.makeText(
                    this,
                    transferFailedText,
                    Toast.LENGTH_SHORT
            ).show();
            Log.i(TAG, "Transfer failed.");
        }
    }

    private void sendTransfer(Eth eth, Transfer tran) throws Exception {
        String senderPrivateKey = getSenderPrivateKey(eth, tran.getSenderAddress());
        Credentials credentials = Credentials.create(senderPrivateKey);
        Future<TransactionReceipt> future = tran.send(credentials);
        future.get();
        String transferSucceededText = "Transfer to " + tran.getReceiverAddress() + " succeeded.";
        Toast.makeText(
                this,
                transferSucceededText,
                Toast.LENGTH_SHORT
        ).show();
        Log.i(TAG, transferSucceededText);
    }

    Eth selectEthFromAccountByAddress(Account account, Transfer tran) throws NoSuchElementException {
        String senderAddress = tran.getSenderAddress();
        for ( Eth eth : account.getEths() ){
            if ( eth.getAddress().equals( senderAddress ) ){
                return eth;
            }
        }
        throw new NoSuchElementException();
    }

    String getSenderPrivateKey(Eth eth, String senderAddress) throws Exception{
        EthDbAdapter db = new EthDbAdapter(this);
        String senderPrivateKey = db.retrieveSenderPrivateKey(eth.getId(), senderAddress);
        db.close();
        return senderPrivateKey;
    }

    Transfer getReplyTransferMessage(){
        Transfer tran = null;
        Intent intent = getIntent();
        Boolean isReplyTransfer = intent.getType() != null && intent.getType().equals("application/" + getPackageName() + "/reply_transfer");
        if ( isReplyTransfer ){
            NdefMessage msg = (NdefMessage)intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)[0];
            tran = SerializationUtils.deserialize(msg.getRecords()[0].getPayload());
            Toast.makeText(this, tran.toString(), Toast.LENGTH_LONG).show();
        }
        return tran;
    }

}
