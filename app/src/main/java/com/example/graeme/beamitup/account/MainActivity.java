package com.example.graeme.beamitup.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.graeme.beamitup.R;
import com.example.graeme.beamitup.Session;
import com.example.graeme.beamitup.transfer.LandingPageActivity;

public class MainActivity extends Activity {
    private static final int REQUEST_LOGIN = 0;
    private static final int REQUEST_SIGNUP = 1;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_sign_in = (Button)findViewById(R.id.btn_sign_in);
        Button btn_create_account = (Button)findViewById(R.id.btn_create_account);

        btn_sign_in.setOnClickListener(v -> {
            Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
            btn_sign_in.setEnabled(false);

            EditText et_email = (EditText)findViewById(R.id.et_email);
            EditText et_password = (EditText)findViewById(R.id.et_password);

            loginIntent.putExtra("email", et_email.getText().toString());
            loginIntent.putExtra("password", et_password.getText().toString());

            startActivityForResult(loginIntent, REQUEST_LOGIN);
        });

        btn_create_account.setOnClickListener(v -> {
            Intent createAccountIntent = new Intent(getApplicationContext(), CreateAccountActivity.class);
            startActivityForResult(createAccountIntent, REQUEST_SIGNUP);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_LOGIN || requestCode == REQUEST_SIGNUP){
            switch (resultCode){
                case RESULT_OK:
                    //Finish activity and log them in
                    Log.d(TAG, "Session account email: " + Session.getUserDetails().getEmail());
                    onLoginSuccess();
                    break;
                case RESULT_CANCELED:
                    onLoginFail();
                    break;
            }
        }
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

    private void onLoginSuccess(){
        Button btn_sign_in = (Button)findViewById(R.id.btn_sign_in);
        btn_sign_in.setEnabled(true);

        final Intent landingPageIntent = new Intent(this, LandingPageActivity.class);
        startActivity(landingPageIntent);
    }

    private void onLoginFail() {
        Button btn_sign_in = (Button)findViewById(R.id.btn_sign_in);
        btn_sign_in.setEnabled(true);
    }

}
