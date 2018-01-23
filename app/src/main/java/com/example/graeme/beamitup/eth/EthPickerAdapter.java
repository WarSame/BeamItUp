package com.example.graeme.beamitup.eth;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.graeme.beamitup.R;

import java.util.List;

public class EthPickerAdapter extends ArrayAdapter {

    private List<Eth> eths;

    EthPickerAdapter(@NonNull Context context, @NonNull List<Eth> eths) {
        super(context, 0, eths);
        this.eths = eths;
    }

    @Override
    public int getCount() {
        return eths.size();
    }

    @Override
    public Object getItem(int position) {
        return eths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return eths.get(position).getId();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Eth eth = (Eth)getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item_eth,
                    parent,
                    false
            );
        }
        TextView tv_address = (TextView)convertView.findViewById(R.id.tv_eth_address);
        TextView tv_id = (TextView)convertView.findViewById(R.id.tv_eth_id);

        tv_address.setText( eth.getAddress() );
        tv_id.setText( Long.toString( eth.getId() ) );
        return convertView;
    }
}