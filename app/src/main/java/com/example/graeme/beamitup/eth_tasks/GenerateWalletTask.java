package com.example.graeme.beamitup.eth_tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.graeme.beamitup.eth.Eth;
import com.example.graeme.beamitup.wallet.WalletHelper;

public class GenerateWalletTask extends AsyncTask<Void, Void, Eth> {
    private static final String TAG = "GenerateWalletTask";
    private Context context;
    private String nickname;

    private GenerateWalletResponse generateWalletResponse;

    public interface GenerateWalletResponse{
        void generateWalletFinish(Eth eth);
    }

    public GenerateWalletTask(
            Context context,
            String nickname,
            GenerateWalletResponse generateWalletResponse
    ){
        this.context = context;
        this.nickname = nickname;
        this.generateWalletResponse = generateWalletResponse;
    }

    @Override
    protected Eth doInBackground(Void... voids) {
        try {
            return WalletHelper.generateWallet(
                    this.context,
                    this.nickname
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
