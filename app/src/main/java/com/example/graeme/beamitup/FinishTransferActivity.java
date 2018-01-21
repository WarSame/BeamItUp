package com.example.graeme.beamitup;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.commons.lang3.SerializationUtils;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import com.example.graeme.beamitup.SendTransferTask.*;

import java.util.NoSuchElementException;

public class FinishTransferActivity extends Activity {
    private static final String TAG = "FinishTransferActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_transfer);
        getActionBar().setDisplayHomeAsUpEnabled(true);

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

    private void sendTransfer(final long ethID, final Transfer tran) throws Exception {
        String senderPrivateKey = getSenderPrivateKey(ethID, tran.getSenderAddress());
        Credentials credentials = Credentials.create(senderPrivateKey);

        SendTransferResponse sendTransferResponse = new SendTransferResponse() {
            @Override
            public void sendTransferFinish(TransactionReceipt transactionReceipt) {
                ProgressBar pbSendTransfer = (ProgressBar)findViewById(R.id.pb_send_transfer);
                pbSendTransfer.setVisibility(View.GONE);
                if (transactionReceipt == null){
                    sendTransferFail(tran);
                }
                else {
                    sendTransferSuccess(transactionReceipt);
                }
            }
        };

        SendTransferTask task = new SendTransferTask(credentials, tran.getReceiverAddress(), sendTransferResponse);
        task.execute(tran);
    }

    String getSenderPrivateKey(long ethID, String senderAddress) throws Exception{
        EthDbAdapter db = new EthDbAdapter(this);
        String senderPrivateKey = db.retrieveSenderPrivateKey(ethID, senderAddress);
        db.close();
        return senderPrivateKey;
    }

    private void sendTransferSuccess(TransactionReceipt transactionReceipt){
        String transferSucceededText = "Transfer to " + transactionReceipt.getTo() + " succeeded.";
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
