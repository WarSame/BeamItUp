package com.example.graeme.beamitup;

import android.app.Fragment;
import android.app.KeyguardManager;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class AuthenticatorFragment extends Fragment {
    private static final String TAG = "AuthenticatorFragment";
    private static final int MOBILE_AUTHENTICATE_REQUEST = 1;
    private KeyguardManager kgm;
    private OnUserAuthenticatedListener onUserAuthenticatedListener;

    public interface OnUserAuthenticatedListener {
        void onUserAuthenticated();
        void onUserNotAuthenticated();
    }

    public AuthenticatorFragment(){
    }

    AuthenticatorFragment setOnUserAuthenticatedListener(OnUserAuthenticatedListener onUserAuthenticatedListener) {
        this.onUserAuthenticatedListener = onUserAuthenticatedListener;
        return this;
    }

    AuthenticatorFragment setKGM(KeyguardManager kgm) {
        if (this.kgm == null){
            Log.e(TAG, "kgm is null");
        }
        this.kgm = kgm;
        return this;
    }

    public void authenticateMobileUser(){
        if (kgm == null){
            Log.e(TAG, "KGM is null");
        }
        Intent credIntent = kgm.createConfirmDeviceCredentialIntent("Authenticate user", "Authenticate with your phone credentials");
        startActivityForResult(credIntent, MOBILE_AUTHENTICATE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        Log.i(TAG, "requestCode = " + requestCode + " resultCode = " + resultCode);
        switch (requestCode){
            case MOBILE_AUTHENTICATE_REQUEST:
                Log.i(TAG, "Handling authentication request response");
                switch (resultCode){
                    case RESULT_OK:
                        Log.i(TAG, "User is authenticated");
                        this.onUserAuthenticatedListener.onUserAuthenticated();
                        break;
                    case RESULT_CANCELED:
                        Toast.makeText(getActivity().getApplicationContext(), "User failed to authenticate", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "User is not authenticated");
                        this.onUserAuthenticatedListener.onUserNotAuthenticated();
                        break;
                }
                break;
        }
    }

    public static class AuthenticatorFragmentBuilder {
        private KeyguardManager kgm;
        private OnUserAuthenticatedListener onUserAuthenticatedListener;

        public AuthenticatorFragment build(){
            return new AuthenticatorFragment()
                .setKGM(this.kgm)
                .setOnUserAuthenticatedListener(onUserAuthenticatedListener);
        }

        public AuthenticatorFragmentBuilder onUserAuthenticatedListener(OnUserAuthenticatedListener onUserAuthenticatedListener) {
            this.onUserAuthenticatedListener = onUserAuthenticatedListener;
            return this;
        }

        public AuthenticatorFragmentBuilder KGM(KeyguardManager kgm) {
            if (this.kgm == null){
                Log.e(TAG, "kgm is null");
            }
            this.kgm = kgm;
            return this;
        }
    }

}
