package com.example.graeme.beamitup.account;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.graeme.beamitup.R;
import com.example.graeme.beamitup.Session;
import com.example.graeme.beamitup.request.ReceiveRequestActivity;
import com.example.graeme.beamitup.LandingPageActivity;

import java.security.NoSuchAlgorithmException;

public class LoginFragment extends Fragment
{
    private static final String TAG = "LoginFragment";
    private Button btn_sign_in;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_login, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText et_email = (EditText) view.findViewById(R.id.et_email);
        EditText et_password = (EditText) view.findViewById(R.id.et_password);
        btn_sign_in = (Button) view.findViewById(R.id.btn_sign_in);

        btn_sign_in.setOnClickListener(v -> {
            btn_sign_in.setEnabled(false);
            String email = et_email.getText().toString();
            char[] password = et_password.getText().toString().toCharArray();
            login(email, password);
        });
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
            Toast.makeText(getContext(), "Username and password combination not found.", Toast.LENGTH_LONG).show();
            onLoginFail();
        }
    }

    Account retrieveAccount(String email){
        AccountDbAdapter db = new AccountDbAdapter(getContext());
        Account account = db.retrieveAccount(email);
        db.close();
        return account;
    }

    private void onLoginSuccess(Account account)
    {
        Session.createSession(account);
        boolean isStartedForResult = getActivity().getCallingActivity() != null;
        if (isStartedForResult) {
            Log.i(TAG, "Returning login result to calling activity: " + getActivity().getCallingActivity());
            Intent returnIntent = new Intent(getActivity(), getActivity().getCallingActivity().getClass());
            getActivity().setResult(ReceiveRequestActivity.RESULT_OK, returnIntent);
            getActivity().finish();
        }
        else {
            Log.i(TAG, "Logging in regularly");
            final Intent landingPageIntent = new Intent(getActivity(), LandingPageActivity.class);
            startActivity(landingPageIntent);
        }
        btn_sign_in.setEnabled(true);
    }

    private void onLoginFail()
    {
        btn_sign_in.setEnabled(true);
    }

    boolean isValid(String email, char[] password)
    {
        return emailValid(email) && passwordValid(password);
    }

    private boolean emailValid(String email)
    {
        boolean valid = true;
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(getContext(), "Enter a valid email.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isEmailInUse(email)){
            Toast.makeText(getContext(), "Email not in use.", Toast.LENGTH_SHORT).show();
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
                    getContext(),
                    "Password must be between " + Account.MINIMUM_PASSWORD_LENGTH + " and " +
                    Account.MAXIMUM_PASSWORD_LENGTH + " characters.",
                    Toast.LENGTH_SHORT
            ).show();
            return false;
        }

        return true;
    }

    boolean isAuthentic(String email, char[] password) throws NoSuchAlgorithmException {
        AccountDbAdapter db = new AccountDbAdapter(getContext());
        boolean isAuthentic = db.isAuthentic(email, password);
        db.close();

        return isAuthentic;
    }

    boolean isEmailInUse(String email)
    {
        AccountDbAdapter db = new AccountDbAdapter(getContext());
        boolean inUse = db.isEmailInUse(email);
        db.close();
        return inUse;
    }
}
