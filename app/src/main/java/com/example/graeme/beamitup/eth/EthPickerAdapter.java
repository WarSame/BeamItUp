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
import java.util.Locale;

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
                    R.layout.frag_list_item_eth,
                    parent,
                    false
            );
        }
        TextView tv_id = (TextView)convertView.findViewById(R.id.tv_eth_id);
        TextView tv_nickname = (TextView)convertView.findViewById(R.id.tv_eth_nickname);
        TextView tv_address = (TextView)convertView.findViewById(R.id.tv_eth_address);

        if (eth == null){
            tv_id.setText( String.format( Locale.CANADA, "%d", 0 ) );
            tv_nickname.setText( "" );
            tv_address.setText( "" );
        }
        else {
            tv_id.setText( String.format( Locale.CANADA, "%d", eth.getId() ) );
            tv_nickname.setText( eth.getNickname() );
            tv_address.setText( eth.getAddress() );
        }
        return convertView;
    }
}
