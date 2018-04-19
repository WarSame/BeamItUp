package com.example.graeme.beamitup.eth_tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.graeme.beamitup.wallet.WalletHelper;

import org.web3j.crypto.Credentials;

public class RetrieveWalletTask extends AsyncTask<Void, Void, Credentials> {
    private static final String TAG = "RetrieveWalletTask";
    private Context context;
    private long ethID;
    private RetrieveWalletResponse retrieveWalletResponse;

    public interface RetrieveWalletResponse{
        void retrieveWalletFinish(Credentials credentials);
    }

    public RetrieveWalletTask(
            Context context,
            long ethID,
            RetrieveWalletResponse retrieveWalletResponse
    ){
        Log.i(TAG, "Creating retrieveWalletTask");
        this.context = context;
        this.ethID = ethID;
        this.retrieveWalletResponse = retrieveWalletResponse;
    }

    @Override
    protected Credentials doInBackground(Void... voids) {
        try {
            Log.i(TAG, "Retrieving wallet");
            return WalletHelper.retrieveCredentials(
                    this.context,
                    this.ethID
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
