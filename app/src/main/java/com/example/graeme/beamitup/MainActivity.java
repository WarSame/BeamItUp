package com.example.graeme.beamitup;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
    private static final int REQUEST_LOGIN = 0;
    private static final int REQUEST_SIGNUP = 1;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Transfer tran = new Transfer("5", "because", "");
        tran.sendTransfer();

        Button btn_sign_in = (Button)findViewById(R.id.btn_sign_in);
        Button btn_create_account = (Button)findViewById(R.id.btn_create_account);

        btn_sign_in.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                Button btn_sign_in = (Button)findViewById(R.id.btn_sign_in);
                btn_sign_in.setEnabled(false);

                EditText et_email = (EditText)findViewById(R.id.et_email);
                EditText et_password = (EditText)findViewById(R.id.et_password);

                loginIntent.putExtra("email", et_email.getText().toString());
                loginIntent.putExtra("password", et_password.getText().toString());

                startActivityForResult(loginIntent, REQUEST_LOGIN);
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
        if (requestCode == REQUEST_LOGIN || requestCode == REQUEST_SIGNUP){
            switch (resultCode){
                case RESULT_OK:
                    //Finish activity and log them in
                    Account account = (Account) data.getSerializableExtra("account");
                    onLoginSuccess(account);
                    break;
                case RESULT_CANCELED:
                    onLoginFail();
                    break;
            }
        }
    }

    private void onLoginSuccess(Account account){
        Button btn_sign_in = (Button)findViewById(R.id.btn_sign_in);
        btn_sign_in.setEnabled(true);

        Log.v(TAG, "Successfully logged in.");

        final Intent landingPageIntent = new Intent(this, LandingPageActivity.class);
        landingPageIntent.putExtra("account", account);
        startActivity(landingPageIntent);
    }

    private void onLoginFail() {
        Button btn_sign_in = (Button)findViewById(R.id.btn_sign_in);
        btn_sign_in.setEnabled(true);

        Toast.makeText(this, "Login failed.", Toast.LENGTH_SHORT).show();
    }

}
