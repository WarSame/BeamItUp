package com.example.graeme.beamitup.eth_tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.graeme.beamitup.eth.Eth;
import com.example.graeme.beamitup.wallet.WalletHelper;

import org.web3j.crypto.Credentials;

import java.io.File;

public class RetrieveWalletTask extends AsyncTask<Void, Void, Credentials> {
    private static final String TAG = "RetrieveWalletTask";
    private Eth eth;
    private File walletFile;
    private RetrieveWalletResponse retrieveWalletResponse;

    public interface RetrieveWalletResponse{
        void retrieveWalletFinish(Credentials credentials);
    }

    public RetrieveWalletTask(
            Eth eth,
            File walletFile,
            RetrieveWalletResponse retrieveWalletResponse
    ){
        Log.i(TAG, "Creating retrieveWalletTask");
        this.eth = eth;
        this.walletFile = walletFile;
        this.retrieveWalletResponse = retrieveWalletResponse;
    }

    @Override
    protected Credentials doInBackground(Void... voids) {
        try {
            Log.i(TAG, "Retrieving wallet");
            return WalletHelper.retrieveCredentials(
                    this.eth,
                    this.walletFile
            );
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Credentials credentials){
        retrieveWalletResponse.retrieveWalletFinish(credentials);
    }
}
