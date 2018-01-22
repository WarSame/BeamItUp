package com.example.graeme.beamitup;

import android.os.AsyncTask;
import android.util.Log;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.http.HttpService;

public class DetermineGasPriceTask extends AsyncTask<Void, Void, EthGasPrice> {
    private static final String TAG = "DetermineGasPriceTask";
    private DetermineGasPriceResponse determineGasPriceResponse;

    DetermineGasPriceTask(DetermineGasPriceResponse determineGasPriceResponse){
        this.determineGasPriceResponse = determineGasPriceResponse;
    }

    @Override
    protected EthGasPrice doInBackground(Void... voids) {
        Log.i(TAG, "Determining gas price async");
        Web3j web3j = Web3jFactory.build(
                new HttpService("https://rinkeby.infura.io/SxLC8uFzMPfzwnlXHqx9")
        );
        try {
            return web3j.ethGasPrice().sendAsync().get();
        } catch ( Exception e ){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(EthGasPrice ethGasPrice) {
        super.onPostExecute(ethGasPrice);
        determineGasPriceResponse.determineGasPriceFinish(ethGasPrice);
    }

    public interface DetermineGasPriceResponse {
        void determineGasPriceFinish(EthGasPrice ethGasPrice);
    }
}
