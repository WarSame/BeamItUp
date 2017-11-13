package com.example.graeme.beamitup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;

import static com.example.graeme.beamitup.Account.startNewLine;

public class LoginActivity extends Activity {
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String email = getIntent().getStringExtra("email");
        String password = getIntent().getStringExtra("password");

        login(email, password);
    }

    private void login(String email, String password) {
        StringBuilder errors = new StringBuilder();

        if (!isValid(email, password, errors)){
            Toast.makeText(this, errors, Toast.LENGTH_LONG).show();
            onLoginFail();
            return;
        }

        Account account;
        try {
            account = new Account(this, email, password);
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "No such algorithm.");
            e.printStackTrace();
            onLoginFail();
            return;
        }

        if (isAuthentic(account)){
            onLoginSuccess(account);
        }
        else {
            Log.v(TAG, "Authentication failed.");
            Toast.makeText(this, "Username and password combination not found.", Toast.LENGTH_LONG).show();
            onLoginFail();
        }
    }

    private void onLoginSuccess(Account account){
        Intent resultIntent = new Intent();
        resultIntent.putExtra("account", account);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void onLoginFail() {
        Intent resultIntent = new Intent();
        setResult(RESULT_CANCELED, resultIntent);
        finish();
    }

    boolean isValid(String email, String password, StringBuilder errors){
        return emailValid(email, errors) && passwordValid(password, errors);
    }

    private boolean emailValid(String email, StringBuilder errors){
        boolean valid = true;
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            startNewLine(errors);
            errors.append("Enter a valid email.");
            return false;
        }

        if (!isEmailInUse(email)){
            startNewLine(errors);
            errors.append("Email not in use.");
            valid = false;
        }
        return valid;
    }

    private boolean passwordValid(String password, StringBuilder errors){
        boolean valid = true;

        if (password.isEmpty() || password.length() < Account.MINIMUM_PASSWORD_LENGTH || password.length() > Account.MAXIMUM_PASSWORD_LENGTH){
            startNewLine(errors);
            errors.append("Password must be between " + Account.MINIMUM_PASSWORD_LENGTH + " and " +
                    Account.MAXIMUM_PASSWORD_LENGTH + " characters.");
            valid = false;
        }

        return valid;
    }

    boolean isAuthentic(Account account) {
        AccountDbAdapter db = new AccountDbAdapter(this);
        boolean isAuthentic = db.isAuthentic(account);
        db.close();

        return isAuthentic;
    }

    boolean isEmailInUse(String email) {
        AccountDbAdapter db = new AccountDbAdapter(this);
        boolean inUse = db.isEmailInUse(email);
        db.close();
        return inUse;
    }
}
