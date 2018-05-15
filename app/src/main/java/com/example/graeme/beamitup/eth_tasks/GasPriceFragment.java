package com.example.graeme.beamitup.eth_tasks;

import android.app.Fragment;
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

public class GasPriceFragment extends Fragment {
    private static final String TAG = "GasPriceFragment";

    public GasPriceFragment(){
    }

    public String getTransactionGasCost(){
        Log.i(TAG, "Determining gas price async");
        Web3j web3j = ((BeamItUp)getActivity().getApplication()).getWeb3j();
        try {
            EthGasPrice ethGasPrice = web3j.ethGasPrice().sendAsync().get();
            return getGasCost(ethGasPrice);
        } catch ( Exception e ){
            e.printStackTrace();
        }
        return "0";
    }

    private String getGasCost(EthGasPrice ethGasPrice){
        BigInteger currentGasPrice = ethGasPrice.getGasPrice();
        final BigInteger GAS_USED = org.web3j.tx.Transfer.GAS_LIMIT;

        BigInteger gasCost = GAS_USED.multiply(currentGasPrice);
        return Convert.fromWei(new BigDecimal(gasCost), Convert.Unit.ETHER ).toString();
    }
}
