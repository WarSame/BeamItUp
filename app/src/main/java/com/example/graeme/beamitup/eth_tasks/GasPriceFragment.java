package com.example.graeme.beamitup.eth_tasks;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.graeme.beamitup.BeamItUp;
import com.example.graeme.beamitup.R;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.frag_gas_cost, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TextView tv_gas_price_value = getActivity().findViewById(R.id.tv_gas_cost_value);
        //tv_gas_price_value.setText(getTransactionGasCost());
    }

    private String getTransactionGasCost(){
        Log.i(TAG, "Determining gas cost for one transaction");
        BeamItUp beamItUp = (BeamItUp) getActivity().getApplication();
        Web3j web3j = beamItUp.getWeb3j();
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
