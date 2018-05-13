package com.example.graeme.beamitup.request;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graeme.beamitup.BeamItUp;
import com.example.graeme.beamitup.LandingPageActivity;
import com.example.graeme.beamitup.R;
import com.example.graeme.beamitup.eth.DaoSession;
import com.example.graeme.beamitup.eth.Eth;
import com.example.graeme.beamitup.eth.EthDao;
import com.example.graeme.beamitup.eth_tasks.FulfillRequestTask;
import com.example.graeme.beamitup.eth_tasks.RetrieveWalletTask;
import com.example.graeme.beamitup.eth_tasks.SendTransactionTask.SendTransactionResponse;
import com.example.graeme.beamitup.wallet.WalletHelper;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Convert;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;

import static com.example.graeme.beamitup.BeamItUp.getWeb3j;

public class FinishRequestActivity extends Activity {
    private static final String TAG = "FinishRequestActivity";

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
        DaoSession daoSession = ((BeamItUp)getApplication()).getDaoSession();
        EthDao ethDao = daoSession.getEthDao();
        long ethID = request.getFromID();
        Eth eth = ethDao.load(ethID);

        File walletFile = WalletHelper.getWalletFile(this, eth.getWalletName());

        RetrieveWalletTask retrieveWalletTask = new RetrieveWalletTask(
                eth,
                walletFile,
                (Credentials credentials)->{
                    if (credentials == null){
                        Log.i(TAG, "Failed to retrieve wallet");
                    }
                    else {
                        Log.i(TAG, "Retrieved wallet");
                        sendTransaction(request, credentials);
                    }
                }
        );

        retrieveWalletTask.execute();
    }

    private void sendTransaction(Request request, Credentials credentials){
        SendTransactionResponse sendTransactionResponse = transactionReceipt -> {
            if (transactionReceipt == null){
                finishRequestFail(request);
            }
            else {
                removeProgressBar();
                finishRequestSuccess(transactionReceipt);
            }
        };

        FulfillRequestTask task = new FulfillRequestTask(
                getWeb3j(),
                credentials,
                sendTransactionResponse
        );
        task.execute(request);
    }

    private void finishRequestSuccess(TransactionReceipt transactionReceipt) {
        Toast.makeText(
                this,
                "Request from " + transactionReceipt.getTo() + " fulfilled.",
                Toast.LENGTH_LONG
        ).show();

        TextView tvSenderAddress = findViewById(R.id.tv_sender_address_value);
        TextView tvReceiverAddress = findViewById(R.id.tv_receiver_address_value);
        TextView tvAmount = findViewById(R.id.tv_amount_value);

        tvSenderAddress.setText(transactionReceipt.getFrom());
        tvReceiverAddress.setText(transactionReceipt.getTo());
        Web3j web3j = getWeb3j();
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
                "Request from " + request.getFromID() + " not fulfilled.",
                Toast.LENGTH_LONG
        ).show();
        Intent landingPageIntent = new Intent(this, LandingPageActivity.class);
        startActivity(landingPageIntent);
    }

    private void removeProgressBar(){
        ProgressBar pbSendTransfer = findViewById(R.id.pb_send_transfer);
        pbSendTransfer.setVisibility(View.GONE);
    }

    private String getTransactionAmount(Transaction transaction){
        BigInteger amount = transaction.getValue();
        BigDecimal amountInEth = Convert.fromWei(new BigDecimal(amount), Convert.Unit.ETHER);
        return amountInEth.toString();
    }



}
