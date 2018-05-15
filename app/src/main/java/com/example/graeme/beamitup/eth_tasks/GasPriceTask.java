package com.example.graeme.beamitup.eth_tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.graeme.beamitup.BeamItUp;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;

public class GasPriceTask extends AsyncTask<Void, Void, String> {
    private static final String TAG = "DetermineGasPriceTask";
    private DetermineGasPriceResponse determineGasPriceResponse;
    private Web3j web3j;

    public GasPriceTask(
            DetermineGasPriceResponse determineGasPriceResponse,
            Web3j web3j
    ){
        this.determineGasPriceResponse = determineGasPriceResponse;
        this.web3j = web3j;
    }

    @Override
    protected String doInBackground(Void... voids) {
        Log.i(TAG, "Determining gas price async");
        try {
            EthGasPrice ethGasPrice = this.web3j.ethGasPrice().sendAsync().get();
            return getGasCost(ethGasPrice);
        } catch ( Exception e ){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String gasPrice) {
        super.onPostExecute(gasPrice);

        Log.i(TAG, "gasPrice is " + gasPrice);

        determineGasPriceResponse.determineGasPriceFinish(gasPrice);
    }

    public interface DetermineGasPriceResponse {
        void determineGasPriceFinish(String gasPrice);
    }

    private String getGasCost(EthGasPrice ethGasPrice){
        BigInteger currentGasPrice = ethGasPrice.getGasPrice();
        final BigInteger GAS_USED = org.web3j.tx.Transfer.GAS_LIMIT;

        BigInteger gasCost = GAS_USED.multiply(currentGasPrice);
        return Convert.fromWei(new BigDecimal(gasCost), Convert.Unit.ETHER ).toString();
    }


}
