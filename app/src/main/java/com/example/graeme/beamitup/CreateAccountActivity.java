package com.example.graeme.beamitup;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;

import static com.example.graeme.beamitup.Account.startNewLine;

public class CreateAccountActivity extends Activity {
    private static final String TAG = "CreateAccountActivity";

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

        if (!isValid(email, password, confirmPassword, errors)){
            Toast.makeText(this, errors, Toast.LENGTH_LONG).show();
            onCreateAccountFail();
            return;
        }

        Account account;
        try {
            account = new Account(this, email, password);
            AccountDbAdapter db = new AccountDbAdapter(this);
            db.createAccount(account);
            db.close();
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "No such algorithm.");
            e.printStackTrace();
            onCreateAccountFail();
            return;
        }
        onCreateAccountSuccess(account);
    }

    private void onCreateAccountSuccess(Account account){
        Button btn_create_account = (Button) findViewById(R.id.btn_create_account);
        btn_create_account.setEnabled(true);

        AccountDbAdapter db = new AccountDbAdapter(this);
        Intent resultIntent = new Intent();
        try {
            Log.v(TAG, "Successfully created account.");
            resultIntent.putExtra("account", account);
            setResult(RESULT_OK, resultIntent);
        } catch (SQLException e){
            Log.e(TAG, "Account creation error.");
            e.printStackTrace();
            setResult(RESULT_CANCELED, resultIntent);
        }
        finally {
            db.close();
            finish();
        }
    }

    private void onCreateAccountFail(){
        Button btn_create_account = (Button) findViewById(R.id.btn_create_account);
        btn_create_account.setEnabled(true);

        Toast.makeText(this, "Create account failed.", Toast.LENGTH_LONG).show();
    }

    boolean isValid(String email, String password, String confirmPassword, StringBuilder errors){
        return emailValid(email, errors) && passwordValid(password, confirmPassword, errors);
    }

    private boolean emailValid(String email, StringBuilder errors) {
        boolean valid = true;

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            startNewLine(errors);
            errors.append("Enter a valid email.");
            return false;
        }

        if (isEmailInUse(email)){
            startNewLine(errors);
            errors.append("Email in use - choose another email.");
            valid = false;
        }
        return valid;
    }

    private boolean passwordValid(String password, String confirmPassword, StringBuilder errors) {
        boolean valid = true;

        if (password.length() < Account.MINIMUM_PASSWORD_LENGTH || password.length() > Account.MAXIMUM_PASSWORD_LENGTH){
            errors.append("Password must be between " + Account.MINIMUM_PASSWORD_LENGTH + " and " +
                    Account.MAXIMUM_PASSWORD_LENGTH + " characters.");
            return false;
        }

        if (!password.equals(confirmPassword)){
            startNewLine(errors);
            errors.append("Password and password confirmation must match.");
            valid = false;
        }

        return valid;
    }

    boolean isEmailInUse(String email) {
        AccountDbAdapter db = new AccountDbAdapter(this);
        boolean inUse = db.isEmailInUse(email);
        db.close();
        return inUse;
    }
}
