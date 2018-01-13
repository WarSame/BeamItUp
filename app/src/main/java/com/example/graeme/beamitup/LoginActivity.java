package com.example.graeme.beamitup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;

public class LoginActivity extends Activity
{
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        String email = getIntent().getStringExtra("email");
        char[] password = (getIntent().getStringExtra("password")).toCharArray();

        login(email, password);
    }

    private void login(String email, char[] password)
    {
        if (!isValid(email, password)){
            onLoginFail();
            return;
        }

        boolean isAuthentic;
        try {
            isAuthentic = isAuthentic(email, password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            onLoginFail();
            return;
        }
        if (isAuthentic){
            Account account = retrieveAccount(email);
            onLoginSuccess(account);
        }
        else {
            Log.v(TAG, "Authentication failed.");
            Toast.makeText(this, "Username and password combination not found.", Toast.LENGTH_LONG).show();
            onLoginFail();
        }
    }

    Account retrieveAccount(String email){
        AccountDbAdapter db = new AccountDbAdapter(this);
        Account account = db.retrieveAccount(email);
        db.close();
        return account;
    }

    private void onLoginSuccess(Account account)
    {
        Session.createSession(account);
        Intent resultIntent = new Intent();
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void onLoginFail()
    {
        Intent resultIntent = new Intent();
        setResult(RESULT_CANCELED, resultIntent);
        finish();
    }

    boolean isValid(String email, char[] password)
    {
        return emailValid(email) && passwordValid(password);
    }

    private boolean emailValid(String email)
    {
        boolean valid = true;
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Enter a valid email.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isEmailInUse(email)){
            Toast.makeText(this, "Email not in use.", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        return valid;
    }

    private boolean passwordValid(char[] password)
    {
        boolean valid = password.length < Account.MINIMUM_PASSWORD_LENGTH
                || password.length > Account.MAXIMUM_PASSWORD_LENGTH;

        if (valid){
            Toast.makeText(
                    this,
                    "Password must be between " + Account.MINIMUM_PASSWORD_LENGTH + " and " +
                    Account.MAXIMUM_PASSWORD_LENGTH + " characters.",
                    Toast.LENGTH_SHORT
            ).show();
            return false;
        }

        return true;
    }

    boolean isAuthentic(String email, char[] password) throws NoSuchAlgorithmException {
        AccountDbAdapter db = new AccountDbAdapter(this);
        boolean isAuthentic = db.isAuthentic(email, password);
        db.close();

        return isAuthentic;
    }

    boolean isEmailInUse(String email)
    {
        AccountDbAdapter db = new AccountDbAdapter(this);
        boolean inUse = db.isEmailInUse(email);
        db.close();
        return inUse;
    }
}
