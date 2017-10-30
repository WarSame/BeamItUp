package com.example.graeme.beamitup;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;

public class LandingPageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        Button btn_transfer_money = (Button)findViewById(R.id.btn_transfer_money);
        Button btn_transfer_history = (Button)findViewById(R.id.btn_transfer_history);

        final Intent createTransferIntent = new Intent(this, CreateTransferActivity.class);
        Account account = (Account) getIntent().getSerializableExtra("account");
        createTransferIntent.putExtra("account", account);

        btn_transfer_money.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(createTransferIntent);
            }
        });

        btn_transfer_history.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
            }
        });
    }

}
