package com.example.graeme.beamitup;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateAccountActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Button btn_create_account = (Button)findViewById(R.id.btn_create_account);
        btn_create_account.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    createAccount();
                }
        });
    }

    private void createAccount() {
        Button btn_create_account = (Button) findViewById(R.id.btn_create_account);
        btn_create_account.setEnabled(false);

        EditText et_email = (EditText)findViewById(R.id.et_email);
        EditText et_password = (EditText) findViewById(R.id.et_password);
        EditText et_confirm_password = (EditText) findViewById(R.id.et_confirm_password);

        String email = et_email.getText().toString();
        String password = et_password.getText().toString();
        String confirmPassword = et_confirm_password.getText().toString();
        StringBuilder errors = new StringBuilder();

        if (!Account.isValidCreateAccount(email, password, confirmPassword, errors)){
            onCreateAccountFail();
            return;
        }
        onCreateAccountSuccess();
    }

    private void onCreateAccountSuccess(){
        Button btn_create_account = (Button) findViewById(R.id.btn_create_account);
        btn_create_account.setEnabled(true);

        setResult(RESULT_OK);
        finish();
    }

    private void onCreateAccountFail(){
        Button btn_create_account = (Button) findViewById(R.id.btn_create_account);
        btn_create_account.setEnabled(true);

        Toast.makeText(this, "Create account failed", Toast.LENGTH_LONG).show();
    }

}
