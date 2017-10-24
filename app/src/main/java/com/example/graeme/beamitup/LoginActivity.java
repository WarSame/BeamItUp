package com.example.graeme.beamitup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

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
                //startActivityForResult(createAccountIntent, REQUEST_SIGNUP);
            }
        });
    }

    private void login() {
        Button btn_sign_in = (Button)findViewById(R.id.btn_sign_in);
        btn_sign_in.setEnabled(false);

        EditText et_email = (EditText)findViewById(R.id.et_email);
        EditText et_password = (EditText)findViewById(R.id.et_password);

        String email = et_email.getText().toString();
        String password = et_password.getText().toString();

        if (!validate(email, password)){
            onLoginFail();
            return;
        }

        //Implement authentication logic - dummy for now
        if (et_email.getText().toString().equals("foo@t.ca") && et_password.getText().toString().equals("barr")){
            onLoginSuccess();
        }
        else {
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
        Toast.makeText(this, "Login failed", Toast.LENGTH_LONG).show();
        btn_sign_in.setEnabled(true);
    }

    private boolean validate(String email, String password) {
        boolean valid = true;

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Enter a valid email", Toast.LENGTH_LONG).show();
            valid = false;
        }
        if (password.isEmpty() || password.length() < 4 || password.length() > 16){
            Toast.makeText(this, "Enter a password between 4 and 16 characters", Toast.LENGTH_LONG).show();
            valid = false;
        }
        return valid;
    }
}
