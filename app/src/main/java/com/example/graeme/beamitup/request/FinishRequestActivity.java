package com.example.graeme.beamitup.request;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ProgressBar;

import com.example.graeme.beamitup.R;
import com.example.graeme.beamitup.eth.Eth;
import com.example.graeme.beamitup.eth.EthDbAdapter;
import com.example.graeme.beamitup.transfer.SendTransferTask;
import com.example.graeme.beamitup.transfer.Transfer;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

public class FinishRequestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_request);

        Intent incomingIntent = getIntent();
        Request request = (Request)incomingIntent.getSerializableExtra("request");
        Eth eth = (Eth)incomingIntent.getSerializableExtra("eth");
        try {
            sendTransfer(eth.getId(), request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendTransfer(final long ethID, final Request request) throws Exception {
        String senderPrivateKey = getSenderPrivateKey(ethID, request.getFromAddress());
        Credentials credentials = Credentials.create(senderPrivateKey);

        SendTransferTask.SendTransferResponse sendTransferResponse = transactionReceipt -> {
            ProgressBar pbSendTransfer = (ProgressBar)findViewById(R.id.pb_send_transfer);
            pbSendTransfer.setVisibility(View.GONE);
            if (transactionReceipt == null){
                finishRequestFail(request);
            }
            else {
                finishRequestSuccess(transactionReceipt);
            }
        };

        FulfillRequestTask task = new FulfillRequestTask(credentials, request.getFromAddress(), sendTransferResponse);
        task.execute(request);
    }

    private void finishRequestSuccess(TransactionReceipt transactionReceipt) {
    }

    private void finishRequestFail(Request request) {
    }

    String getSenderPrivateKey(long ethID, String senderAddress) throws Exception{
        EthDbAdapter db = new EthDbAdapter(this);
        String senderPrivateKey = db.retrieveSenderPrivateKey(ethID, senderAddress);
        db.close();
        return senderPrivateKey;
    }



}
