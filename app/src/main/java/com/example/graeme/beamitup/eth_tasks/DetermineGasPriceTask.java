package com.example.graeme.beamitup.eth_tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.graeme.beamitup.Session;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;

public class DetermineGasPriceTask extends AsyncTask<Void, Void, EthGasPrice> {
    private static final String TAG = "DetermineGasPriceTask";
    private DetermineGasPriceResponse determineGasPriceResponse;

    public DetermineGasPriceTask(DetermineGasPriceResponse determineGasPriceResponse){
        this.determineGasPriceResponse = determineGasPriceResponse;
    }

    @Override
    protected EthGasPrice doInBackground(Void... voids) {
        Log.i(TAG, "Determining gas price async");
        Web3j web3j = Session.getWeb3j();
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

        String gasCost = getCurrentGasCost(ethGasPrice);
        determineGasPriceResponse.determineGasPriceFinish(gasCost);
    }

    public interface DetermineGasPriceResponse {
        void determineGasPriceFinish(String ethGasPrice);
    }

    private String getCurrentGasCost(EthGasPrice ethGasPrice){
        BigInteger currentGasPrice = ethGasPrice.getGasPrice();
        BigInteger GAS_USED = org.web3j.tx.Transfer.GAS_LIMIT;

        BigInteger gasCost = GAS_USED.multiply(currentGasPrice);
        return Convert.fromWei(new BigDecimal(gasCost), Convert.Unit.ETHER ).toString();
    }


}
