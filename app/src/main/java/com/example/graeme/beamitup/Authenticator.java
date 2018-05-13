package com.example.graeme.beamitup;

import android.app.Fragment;
import android.app.KeyguardManager;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class Authenticator extends Fragment {
    private static final String TAG = "Authenticator";
    private static final int MOBILE_AUTHENTICATE_REQUEST = 1;
    private KeyguardManager kgm;
    private OnUserAuthenticatedListener onUserAuthenticatedListener;

    public interface OnUserAuthenticatedListener {
        void onUserAuthenticated();
        void onUserNotAuthenticated();
    }

    public Authenticator(){
    }

    public void setOnUserAuthenticatedListener(OnUserAuthenticatedListener onUserAuthenticatedListener) {
        this.onUserAuthenticatedListener = onUserAuthenticatedListener;
    }

    public void setKGM(KeyguardManager kgm) {
        if (this.kgm == null){
            Log.e(TAG, "kgm is null");
        }
        this.kgm = kgm;
    }

    public boolean isDeviceSecure(){
        return kgm.isDeviceSecure();
    }

    public void authenticateMobileUser(){
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
                        Toast.makeText(getActivity().getApplicationContext(), "User authenticated", Toast.LENGTH_LONG).show();
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

}
