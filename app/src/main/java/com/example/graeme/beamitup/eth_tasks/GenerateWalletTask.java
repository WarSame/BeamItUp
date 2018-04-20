package com.example.graeme.beamitup.eth_tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.graeme.beamitup.eth.Eth;
import com.example.graeme.beamitup.eth.EthDbAdapter;
import com.example.graeme.beamitup.wallet.WalletHelper;

import java.io.File;

public class GenerateWalletTask extends AsyncTask<Void, Void, Eth> {
    private static final String TAG = "GenerateWalletTask";
    private EthDbAdapter ethDbAdapter;
    private File walletDir;
    private String nickname;

    private GenerateWalletResponse generateWalletResponse;

    public interface GenerateWalletResponse{
        void generateWalletFinish(Eth eth);
    }

    public GenerateWalletTask(
            File walletDir,
            String nickname,
            EthDbAdapter ethDbAdapter,
            GenerateWalletResponse generateWalletResponse
    ){
        this.walletDir = walletDir;
        this.nickname = nickname;
        this.ethDbAdapter = ethDbAdapter;
        this.generateWalletResponse = generateWalletResponse;
    }

    @Override
    protected Eth doInBackground(Void... voids) {
        try {
            return WalletHelper.generateWallet(
                    this.walletDir,
                    this.nickname,
                    this.ethDbAdapter
            );
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Eth eth){
        Log.i(TAG, "Finished generating wallet.");
        Log.i(TAG, "Wallet name: " + eth.getWalletName());
        Log.i(TAG, "Wallet address: " + eth.getAddress());

        generateWalletResponse.generateWalletFinish(eth);
    }
}
