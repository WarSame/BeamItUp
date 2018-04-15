package com.example.graeme.beamitup.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.graeme.beamitup.LandingPageActivity;
import com.example.graeme.beamitup.R;
import com.example.graeme.beamitup.Session;
import com.example.graeme.beamitup.request.ReceiveRequestActivity;

public class LoginActivity extends Activity {
    private static final String TAG = "LoginActivity";
    Button btn_create_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_create_account = (Button)findViewById(R.id.btn_create_account);

        boolean isStartedForResult = getCallingActivity() != null;
        btn_create_account.setOnClickListener(v->{
            if (isStartedForResult) {
                Log.i(TAG, "Returning login result to calling activity: " + getCallingActivity().getClass());
                Intent returnIntent = new Intent(this, getCallingActivity().getClass());
                setResult(ReceiveRequestActivity.RESULT_OK, returnIntent);
                finish();
            }
            else {
                Log.i(TAG, "Logging in without a result");
                Intent createAccountIntent = new Intent(this, CreateAccountActivity.class);
                startActivity(createAccountIntent);
            }
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
