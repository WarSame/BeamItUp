package com.example.graeme.beamitup.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.graeme.beamitup.R;
import com.example.graeme.beamitup.Session;
import com.example.graeme.beamitup.transfer.LandingPageActivity;

public class LoginActivity extends Activity {
    private static final String TAG = "LoginActivity";
    Button btn_create_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_create_account = (Button)findViewById(R.id.btn_create_account);
        btn_create_account.setOnClickListener(v->{
            Intent createAccountIntent = new Intent(this, CreateAccountActivity.class);
            startActivity(createAccountIntent);
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        if (Session.isAlive()){
            Log.d(TAG, "Session is alive.");
            final Intent landingPageIntent = new Intent(this, LandingPageActivity.class);
            startActivity(landingPageIntent);
        }
        else {
            Log.d(TAG, "Session is dead");
        }
    }

}
