package com.example.graeme.beamitup;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;

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

        if (!isValid(email, password, confirmPassword)){
            onCreateAccountFail();
            return;
        }

        Account account;
        try {
            account = createNewAccount(email, password);
            onCreateAccountSuccess(account);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            onCreateAccountFail();
        }
    }

    private Account createNewAccount(String email, String password) throws NoSuchAlgorithmException {
        byte[] salt = Encryption.generateSalt();
        byte[] passwordHash = Encryption.hashPassword(password, salt);
        AccountDbAdapter db = new AccountDbAdapter(this);
        long accountId = db.createAccount(email, passwordHash, salt);
        db.close();
        return new Account(email, accountId);
    }

    private void onCreateAccountSuccess(Account account){
        Button btn_create_account = (Button) findViewById(R.id.btn_create_account);
        btn_create_account.setEnabled(true);

        AccountDbAdapter db = new AccountDbAdapter(this);
        Intent resultIntent = new Intent();
        try {
            Log.v(TAG, "Successfully created account.");
            Session.createSession(account);
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

    boolean isValid(String email, String password, String confirmPassword){
        return emailValid(email) && passwordValid(password, confirmPassword);
    }

    private boolean emailValid(String email) {
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(
                    this,
                    "Enter a valid email.",
                    Toast.LENGTH_SHORT
            ).show();
            return false;
        }

        if (isEmailInUse(email)){
            Toast.makeText(
                    this,
                    "Email in use - choose another email.",
                    Toast.LENGTH_SHORT
            ).show();
            return false;
        }
        return true;
    }

    private boolean passwordValid(String password, String confirmPassword) {
        if (password.length() < Account.MINIMUM_PASSWORD_LENGTH || password.length() > Account.MAXIMUM_PASSWORD_LENGTH){
            Toast.makeText(
                    this,
                    "Password must be between " + Account.MINIMUM_PASSWORD_LENGTH + " and " +
                    Account.MAXIMUM_PASSWORD_LENGTH + " characters.",
                    Toast.LENGTH_SHORT
            ).show();
            return false;
        }

        if (!password.equals(confirmPassword)){
            Toast.makeText(
                    this,
                    "Password and password confirmation must match.",
                    Toast.LENGTH_SHORT
            ).show();
            return false;
        }

        return true;
    }

    boolean isEmailInUse(String email) {
        AccountDbAdapter db = new AccountDbAdapter(this);
        boolean inUse = db.isEmailInUse(email);
        db.close();
        return inUse;
    }
}
