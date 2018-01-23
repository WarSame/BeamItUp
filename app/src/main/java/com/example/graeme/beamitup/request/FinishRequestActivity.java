package com.example.graeme.beamitup.request;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graeme.beamitup.R;
import com.example.graeme.beamitup.Session;
import com.example.graeme.beamitup.account.Account;
import com.example.graeme.beamitup.eth.Eth;
import com.example.graeme.beamitup.eth.EthDbAdapter;
import com.example.graeme.beamitup.transfer.LandingPageActivity;
import com.example.graeme.beamitup.transfer.SendTransactionTask.SendTransferResponse;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.NoSuchElementException;

public class FinishRequestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_request);

        Intent incomingIntent = getIntent();
        Request request = (Request)incomingIntent.getSerializableExtra("request");
        try {
            sendTransfer(request);
        } catch (Exception e) {
            e.printStackTrace();
            finishRequestFail(request);
        }
    }

    private void sendTransfer(final Request request) throws Exception {
        Account account = Session.getUserDetails();
        Eth eth = selectEthFromAccountByAddress( account, request.getFromAddress() );

        String senderPrivateKey = getSenderPrivateKey(eth.getId(), request.getFromAddress());
        Credentials credentials = Credentials.create(senderPrivateKey);

         SendTransferResponse sendTransferResponse = transactionReceipt -> {
            if (transactionReceipt == null){
                finishRequestFail(request);
            }
            else {
                removeProgressBar();
                finishRequestSuccess(transactionReceipt);
            }
        };

        FulfillRequestTask task = new FulfillRequestTask(credentials, request.getToAddress(), sendTransferResponse);
        task.execute(request);
    }

    Eth selectEthFromAccountByAddress(Account account, String fromAddress) throws NoSuchElementException {
        for ( Eth eth : account.getEths() ){
            if ( eth.getAddress().equals( fromAddress ) ){
                return eth;
            }
        }
        throw new NoSuchElementException();
    }

    private void finishRequestSuccess(TransactionReceipt transactionReceipt) {
        Toast.makeText(
                this,
                "Request from " + transactionReceipt.getTo() + " fulfilled.",
                Toast.LENGTH_LONG
        ).show();

        TextView tvSenderAddress = (TextView)findViewById(R.id.tv_sender_address_value);
        TextView tvReceiverAddress = (TextView)findViewById(R.id.tv_receiver_address_value);
        TextView tvAmount = (TextView)findViewById(R.id.tv_amount_value);

        tvSenderAddress.setText(transactionReceipt.getFrom());
        tvReceiverAddress.setText(transactionReceipt.getTo());
        Web3j web3j = Session.getWeb3j();
        try {
            Transaction transaction = web3j.ethGetTransactionByHash(transactionReceipt.getTransactionHash())
                    .sendAsync().get().getTransaction();
            tvAmount.setText( getTransactionAmount(transaction) );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void finishRequestFail(Request request) {
        Toast.makeText(
                this,
                "Request from " + request.getFromAddress() + " not fulfilled.",
                Toast.LENGTH_LONG
        ).show();
        Intent landingPageIntent = new Intent(this, LandingPageActivity.class);
        startActivity(landingPageIntent);
    }

    private void removeProgressBar(){
        ProgressBar pbSendTransfer = (ProgressBar)findViewById(R.id.pb_send_transfer);
        pbSendTransfer.setVisibility(View.GONE);
    }

    String getSenderPrivateKey(long ethID, String senderAddress) throws Exception{
        EthDbAdapter db = new EthDbAdapter(this);
        String senderPrivateKey = db.retrieveSenderPrivateKey(ethID, senderAddress);
        db.close();
        return senderPrivateKey;
    }

    private String getTransactionAmount(Transaction transaction) throws Exception{
        BigInteger amount = transaction.getValue();
        BigDecimal amountInEth = Convert.fromWei(new BigDecimal(amount), Convert.Unit.ETHER);
        return amountInEth.toString();
    }



}
