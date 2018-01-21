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
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.util.NoSuchElementException;
import java.util.concurrent.Future;

public class FinishTransferActivity extends Activity {
    private static final String TAG = "FinishTransferActivity";
    TransferSenderService transferSenderService;
    boolean bound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_transfer);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent transferSenderIntent = new Intent(this, TransferSenderService.class);
        bindService(transferSenderIntent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(connection);
        bound = false;
    }

    Eth selectEthFromAccountByAddress(Account account, String senderAddress) throws NoSuchElementException {
        for ( Eth eth : account.getEths() ){
            if ( eth.getAddress().equals( senderAddress ) ){
                return eth;
            }
        }
        throw new NoSuchElementException();
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            Log.i(TAG, "Service is connected.");
            TransferSenderService.TransferSenderBinder binder = (TransferSenderService.TransferSenderBinder) service;
            transferSenderService = binder.getService();
            bound = true;

            handleTransfer();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "Service is disconnected.");
            bound = false;
        }
    };

    private void handleTransfer(){
        if (!Session.isAlive()){
            final Intent loginIntent = new Intent(getApplicationContext(), MainActivity.class);
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
            sendTransferFail(tran);
        }
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

    private void sendTransfer(long ethID, Transfer tran) throws Exception {
        String senderPrivateKey = getSenderPrivateKey(ethID, tran.getSenderAddress());
        Credentials credentials = Credentials.create(senderPrivateKey);

        TransferSendTask task = new TransferSendTask();
        task.execute(tran);
    }

    String getSenderPrivateKey(long ethID, String senderAddress) throws Exception{
        EthDbAdapter db = new EthDbAdapter(this);
        String senderPrivateKey = db.retrieveSenderPrivateKey(ethID, senderAddress);
        db.close();
        return senderPrivateKey;
    }

    private void sendTransferSuccess(Transfer tran){
        String transferSucceededText = "Transfer to " + tran.getReceiverAddress() + " succeeded.";
        Toast.makeText(
                this,
                transferSucceededText,
                Toast.LENGTH_SHORT
        ).show();
        Log.i(TAG, transferSucceededText);
    }

    private void sendTransferFail(Transfer tran){
        String transferFailedText = "Transfer to " + tran.getReceiverAddress() + " failed.";
        Toast.makeText(
                this,
                transferFailedText,
                Toast.LENGTH_SHORT
        ).show();
        Log.i(TAG, "Transfer failed.");
    }
}
