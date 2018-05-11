package com.example.graeme.beamitup.eth_tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.graeme.beamitup.wallet.WalletHelper;

import java.io.File;

public class GenerateWalletTask extends AsyncTask<Void, Void, String> {
    private static final String TAG = "GenerateWalletTask";
    private File walletDir;
    private String longPassword;

    private GenerateWalletResponse generateWalletResponse;

    public interface GenerateWalletResponse{
        void generateWalletFinish(String walletName) throws Exception;
    }

    public GenerateWalletTask(
            String longPassword,
            File walletDir,
            GenerateWalletResponse generateWalletResponse
    ){
        this.longPassword = longPassword;
        this.walletDir = walletDir;
        this.generateWalletResponse = generateWalletResponse;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            return WalletHelper.generateWallet(
                    this.longPassword,
                    this.walletDir
            );
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String walletName){
        Log.i(TAG, "Finished generating wallet.");
        Log.i(TAG, "Wallet name: " + walletName);

        try {
            generateWalletResponse.generateWalletFinish(walletName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
