package com.example.graeme.beamitup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
    private static final int REQUEST_SIGNUP = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btn_sign_in = (Button)findViewById(R.id.btn_sign_in);
        Button btn_create_account = (Button)findViewById(R.id.btn_create_account);

        btn_sign_in.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                login();
            }
        });

        btn_create_account.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent createAccountIntent = new Intent(getApplicationContext(), CreateAccountActivity.class);
                startActivityForResult(createAccountIntent, REQUEST_SIGNUP);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_SIGNUP){
            if (resultCode == RESULT_OK){
                //Finish activity and log them in
                onLoginSuccess();
            }
        }
    }

    private void login() {
        Button btn_sign_in = (Button)findViewById(R.id.btn_sign_in);
        btn_sign_in.setEnabled(false);

        EditText et_email = (EditText)findViewById(R.id.et_email);
        EditText et_password = (EditText)findViewById(R.id.et_password);

        String email = et_email.getText().toString();
        String password = et_password.getText().toString();
        StringBuilder errors = new StringBuilder();

        if (!LoginAccount.isValid(email, password, errors)){
            Toast.makeText(this, errors, Toast.LENGTH_LONG).show();
            onLoginFail();
            return;
        }

        if (LoginAccount.isAuthentic(email, password)){
            onLoginSuccess();
        }
        else {
            Toast.makeText(this, "Username and password combination not found.", Toast.LENGTH_LONG).show();
            onLoginFail();
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
