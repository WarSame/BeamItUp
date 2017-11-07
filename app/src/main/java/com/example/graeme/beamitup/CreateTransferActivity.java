package com.example.graeme.beamitup;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class CreateTransferActivity extends Activity{
    Intent readyTransferIntent;
    int lv_position = 0;
    Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_transfer);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        readyTransferIntent = new Intent(this, ReadyTransferActivity.class);
        final ListView lv_select_account = (ListView)findViewById(R.id.lv_transfer_money_account);
        final Button btn_ready_transfer = (Button) findViewById(R.id.btn_ready_transfer);

        account = (Account) getIntent().getSerializableExtra("account");
        account.addEthereumAccount(new Eth("publickey", "privatekey".getBytes()));
        account.addEthereumAccount(new Eth("publickey2", "privatekey2".getBytes()));
        ArrayList<Eth> eths = account.getEths();

        EthAdapter ethAdapter = new EthAdapter(this, eths);
        lv_select_account.setAdapter(ethAdapter);

        lv_select_account.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
                lv_position = position;
            }
        });

        btn_ready_transfer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Eth eth = (Eth)lv_select_account.getItemAtPosition(lv_position);
                readyTransferIntent.putExtra("senderAddress", eth.getAddress());
                readyTransferIntent.putExtra("amount", ((EditText)findViewById(R.id.edittext_transfer_money_amount)).getText().toString());
                readyTransferIntent.putExtra("reason", ((EditText)findViewById(R.id.edittext_transfer_money_reason)).getText().toString());
                startActivity(readyTransferIntent);
            }
        });
    }

    private void onCreateTransferFail(){
        Toast.makeText(this, "Transfer creation failed", Toast.LENGTH_LONG).show();
    }

}
