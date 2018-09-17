package com.example.graeme.beamitup.transaction;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

import com.example.graeme.beamitup.R;

public class TransactionDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);

        Transaction transaction = (Transaction) getIntent().getSerializableExtra("transaction");

        String senderAddress = transaction.getSenderWallet().getAddress();
        String receiverAddress = transaction.getRequest().getToAddress();
        String amount = transaction.getRequest().getAmount();

        TextView tv_sender_address = findViewById(R.id.tv_sender_address_value);
        TextView tv_receiver_address = findViewById(R.id.tv_receiver_address_value);
        TextView tv_amount = findViewById(R.id.tv_amount_value);

        tv_amount.setText(amount);
        tv_sender_address.setText(senderAddress);
        tv_receiver_address.setText(receiverAddress);
    }

}
