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

import java.util.ArrayList;

public class CreateTransferActivity extends Activity{
    Intent readyTransferIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_transfer);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        readyTransferIntent = new Intent(this, ReadyTransferActivity.class);
        ListView lv_select_account = (ListView)findViewById(R.id.lv_transfer_money_account);
        final Button btn_ready_transfer = (Button) findViewById(R.id.btn_ready_transfer);
        btn_ready_transfer.setEnabled(false);

        Account account = (Account) getIntent().getSerializableExtra("account");
        account.addEthereumAccount(new Eth("publickey", "privatekey".getBytes()));
        account.addEthereumAccount(new Eth("publickey2", "privatekey2".getBytes()));
        ArrayList<Eth> eths = account.getEths();

        EthAdapter ethAdapter = new EthAdapter(this, eths);
        lv_select_account.setAdapter(ethAdapter);

        lv_select_account.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
                btn_ready_transfer.setEnabled(true);
            }
        });

        btn_ready_transfer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                readyTransferIntent.putExtra("senderPublicKey", "");
                readyTransferIntent.putExtra("amount", ((EditText)findViewById(R.id.edittext_transfer_money_amount)).getText().toString());
                readyTransferIntent.putExtra("reason", ((EditText)findViewById(R.id.edittext_transfer_money_reason)).getText().toString());
                startActivity(readyTransferIntent);
            }
        });
    }

}
