package com.example.graeme.beamitup.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.graeme.beamitup.R;
import com.example.graeme.beamitup.Session;
import com.example.graeme.beamitup.LandingPageActivity;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class CreateAccountActivity extends Activity {
    private static final String TAG = "CreateAccountActivity";
    Button btn_create_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        btn_create_account = (Button)findViewById(R.id.btn_create_account);
        btn_create_account.setOnClickListener(v -> createAccount());
    }

    private void createAccount() {
        Button btn_create_account = (Button) findViewById(R.id.btn_create_account);
        btn_create_account.setEnabled(false);

        EditText et_email = (EditText)findViewById(R.id.et_email);
        EditText et_password = (EditText) findViewById(R.id.et_password);
        EditText et_confirm_password = (EditText) findViewById(R.id.et_confirm_password);

        String email = et_email.getText().toString();
        char[] password = (et_password.getText().toString()).toCharArray();
        char[] confirmPassword = (et_confirm_password.getText().toString()).toCharArray();

        Boolean isValid = !isValid(email, password, confirmPassword);

        Arrays.fill(confirmPassword, '\0');//Clear confirm password for security

        if (isValid){
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

    private Account createNewAccount(String email, char[] password) throws NoSuchAlgorithmException {
        AccountDbAdapter db = new AccountDbAdapter(this);
        long accountId = db.createAccount(email, password);
        db.close();
        return new Account(email, accountId);
    }

    private void onCreateAccountSuccess(Account account){
        Log.v(TAG, "Successfully created account.");
        btn_create_account.setEnabled(true);

        Session.createSession(account);
        Intent landingPageIntent = new Intent(this, LandingPageActivity.class);
        startActivity(landingPageIntent);
    }

    private void onCreateAccountFail(){
        Button btn_create_account = (Button) findViewById(R.id.btn_create_account);
        btn_create_account.setEnabled(true);

        Toast.makeText(this, "Create account failed.", Toast.LENGTH_LONG).show();
    }

    boolean isValid(String email, char[] password, char[] confirmPassword){
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

    private boolean passwordValid(char[] password, char[] confirmPassword) {
        if (password.length < Account.MINIMUM_PASSWORD_LENGTH || password.length > Account.MAXIMUM_PASSWORD_LENGTH){
            Toast.makeText(
                    this,
                    "Password must be between " + Account.MINIMUM_PASSWORD_LENGTH + " and " +
                    Account.MAXIMUM_PASSWORD_LENGTH + " characters.",
                    Toast.LENGTH_SHORT
            ).show();
            return false;
        }

        if (!Arrays.equals(password, confirmPassword)){
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
