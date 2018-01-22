package com.example.graeme.beamitup.transfer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.graeme.beamitup.account.Account;
import com.example.graeme.beamitup.account.LoginActivity;
import com.example.graeme.beamitup.R;

public class ReceiveTransferActivity extends Activity {
    private static final int REQUEST_LOGIN = 0;
    private static final String TAG = "ReceiveTransferActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_transfer);
        Button btn_sign_in = (Button)findViewById(R.id.btn_sign_in);

        btn_sign_in.setOnClickListener(v -> {
            Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
            Button btn_sign_in1 = (Button)findViewById(R.id.btn_sign_in);
            btn_sign_in1.setEnabled(false);

            EditText et_email = (EditText)findViewById(R.id.et_email);
            EditText et_password = (EditText)findViewById(R.id.et_password);

            String email = et_email.getText().toString();
            String password = et_password.getText().toString();

            loginIntent.putExtra("email", email);
            loginIntent.putExtra("password", password);

            startActivityForResult(loginIntent, REQUEST_LOGIN);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_LOGIN){
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

        final Intent replyTransferIntent = new Intent(this, ReplyTransferActivity.class);
        replyTransferIntent.putExtra("account", account);
        startActivity(replyTransferIntent);
    }

    private void onLoginFail() {
        Button btn_sign_in = (Button)findViewById(R.id.btn_sign_in);
        btn_sign_in.setEnabled(true);

        Toast.makeText(this, "Login failed.", Toast.LENGTH_SHORT).show();
    }
}
