package com.example.graeme.beamitup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class EthAdapter extends ArrayAdapter {
    EthAdapter(Context context, ArrayList<Eth> eths){
        super(context, 0, eths);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent){
        Eth eth = (Eth)getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_eth, parent, false);
        }
        TextView tv_publicKey = (TextView)convertView.findViewById(R.id.tv_eth_address);
        tv_publicKey.setText( eth.getAddress() );
        return convertView;
    }
}
