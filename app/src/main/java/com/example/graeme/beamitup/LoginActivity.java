package com.example.graeme.beamitup;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class LoginActivity extends Activity
{
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        String email = getIntent().getStringExtra("email");
        String password = getIntent().getStringExtra("password");

        login(email, password);
    }

    private void login(String email, String password)
    {
        if (!isValid(email, password)){
            onLoginFail();
            return;
        }

        byte[] passwordHash;
        try {
            passwordHash = retrievePasswordHash(email, password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            onLoginFail();
            return;
        }
        if (isAuthentic(email, passwordHash)){
            Account account = retrieveAccount(email);
            ArrayList<Eth> eths = retrieveEths(account.getId());
            account.setEths(eths);
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

    byte[] retrievePasswordHash(String email, String password) throws NoSuchAlgorithmException {
        AccountDbAdapter db = new AccountDbAdapter(this);
        byte[] salt = db.retrieveSalt(email);
        db.close();
        return Encryption.hashPassword(password, salt);
    }

    ArrayList<Eth> retrieveEths(long accountId)
    {
        EthDbAdapter ethDbAdapter = new EthDbAdapter(this);

        Cursor res = ethDbAdapter.retrieveEthByAccountId(accountId);
        ArrayList<Eth> eths = addEthsToListFromCursor(res);

        ethDbAdapter.close();

        Log.d(TAG, "Account ID: "+ accountId);
        Log.d(TAG, "Number of eth in arraylist: " + eths.size());
        return eths;
    }

    ArrayList<Eth> addEthsToListFromCursor(Cursor res)
    {
        ArrayList<Eth> eths = new ArrayList<>();
        int numEth = res.getCount();
        for (int i = 0; i < numEth; i++){
            Eth eth = getEthFromCursor(res);
            eths.add(eth);
            res.moveToNext();
        }
        return eths;
    }

    Eth getEthFromCursor(Cursor res)
    {
        Eth eth = new Eth();

        long ethId = res.getLong(res.getColumnIndex(DbAdapter.EthTable._ID));
        long accountId = res.getLong(res.getColumnIndex(DbAdapter.EthTable.ETH_ACCOUNT_ID));
        String address = res.getString(res.getColumnIndex(DbAdapter.EthTable.ETH_ADDRESS));
        byte[] encPrivateKey = res.getBlob(res.getColumnIndex(DbAdapter.EthTable.ETH_ENC_PRIVATE_KEY));
        byte[] iv = res.getBlob(res.getColumnIndex(DbAdapter.EthTable.ETH_IV));

        eth.setId(ethId);
        eth.setAccountId(accountId);
        eth.setAddress(address);
        eth.setEncPrivateKey(encPrivateKey);
        eth.setIv(iv);
        Log.d(TAG, "Eth address: " + eth.getAddress());

        return eth;
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

    boolean isValid(String email, String password)
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

    private boolean passwordValid(String password)
    {
        boolean valid = true;

        if (password.isEmpty() || password.length() < Account.MINIMUM_PASSWORD_LENGTH || password.length() > Account.MAXIMUM_PASSWORD_LENGTH){
            Toast.makeText(
                    this,
                    "Password must be between " + Account.MINIMUM_PASSWORD_LENGTH + " and " +
                    Account.MAXIMUM_PASSWORD_LENGTH + " characters.",
                    Toast.LENGTH_SHORT
            ).show();
            valid = false;
        }

        return valid;
    }

    boolean isAuthentic(String email, byte[] passwordHash)
    {
        AccountDbAdapter db = new AccountDbAdapter(this);
        boolean isAuthentic = db.isAuthentic(email, passwordHash);
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
