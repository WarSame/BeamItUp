package com.example.graeme.beamitup;

import android.app.Fragment;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.example.graeme.beamitup.eth.Eth;
import com.example.graeme.beamitup.request.Request;


public class Authenticator extends Fragment {
    private static final String TAG = "Authenticator";
    private static final int MOBILE_AUTHENTICATE_REQUEST = 1;
    private KeyguardManager kgm;
    private OnUserAuthenticatedListener onUserAuthenticatedListener;

    public interface OnUserAuthenticatedListener {
        void onUserAuthenticated(int resultCode);
    }

    public Authenticator(){
        kgm = (KeyguardManager) getActivity().getApplication().getSystemService(Context.KEYGUARD_SERVICE);
        if (kgm == null){
            Log.e(TAG, "kgm is null");
        }
    }

    public void setOnUserAuthenticatedListener(OnUserAuthenticatedListener onUserAuthenticatedListener) {
        this.onUserAuthenticatedListener = onUserAuthenticatedListener;
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
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "requestCode = " + requestCode + " resultCode = " + resultCode);
        switch (requestCode){
            case MOBILE_AUTHENTICATE_REQUEST:
                Log.i(TAG, "Handling authentication request");
                this.onUserAuthenticatedListener.onUserAuthenticated(resultCode);
                break;
        }
    }

}
