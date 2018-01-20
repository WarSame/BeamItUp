package com.example.graeme.beamitup;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.lang3.SerializationUtils;

import java.util.NoSuchElementException;
import com.example.graeme.beamitup.TransferSenderService.TransferSenderBinder;

public class FinishTransferActivity extends Activity {
    private static final String TAG = "FinishTransferActivity";
    TransferSenderService transferSenderService;
    boolean bound = false;

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
            Eth eth = selectEthFromAccountByAddress(account, tran.getSenderAddress());
            sendTransfer(eth.getId(), tran);
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

    private void sendTransfer(long ethID, Transfer tran) throws Exception {
        //Run transfer in background
        Intent transferSender = new Intent(getApplicationContext(), TransferSenderService.class);
        bindService(transferSender, connection, Context.BIND_AUTO_CREATE);

        if (bound){
            int x = transferSenderService.getValue();
            Log.i(TAG, "Value from service: " + x);
        }

        String transferSucceededText = "Transfer to " + tran.getReceiverAddress() + " succeeded.";
        Toast.makeText(
                this,
                transferSucceededText,
                Toast.LENGTH_SHORT
        ).show();
        Log.i(TAG, transferSucceededText);
    }

    Eth selectEthFromAccountByAddress(Account account, String senderAddress) throws NoSuchElementException {
        for ( Eth eth : account.getEths() ){
            if ( eth.getAddress().equals( senderAddress ) ){
                return eth;
            }
        }
        throw new NoSuchElementException();
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

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TransferSenderBinder binder = (TransferSenderBinder) service;
            transferSenderService = binder.getService();
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
        }
    };

}
