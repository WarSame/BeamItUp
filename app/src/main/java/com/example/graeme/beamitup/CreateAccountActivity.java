package com.example.graeme.beamitup;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
        onCreateAccountSuccess(email, password);
    }

    private void onCreateAccountSuccess(String email, String password){
        Button btn_create_account = (Button) findViewById(R.id.btn_create_account);
        btn_create_account.setEnabled(true);

        BeamItUpDbHelper db = new BeamItUpDbHelper(this);
        db.insertAccount(email, password);
        db.close();

        Log.v(TAG, "Successfully created account.");

        setResult(RESULT_OK);
        finish();
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
        BeamItUpDbHelper db = new BeamItUpDbHelper(this);
        boolean inUse = db.isEmailInUse(email);
        db.close();
        return inUse;
    }
}
