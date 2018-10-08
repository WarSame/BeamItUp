package com.example.graeme.beamitup.wallet;

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

public class WalletPickerAdapter extends ArrayAdapter {

    private List<Wallet> wallets;

    WalletPickerAdapter(@NonNull Context context, @NonNull List<Wallet> wallets) {
        super(context, 0, wallets);
        this.wallets = wallets;
    }

    @Override
    public int getCount() {
        return wallets.size();
    }

    @Override
    public Object getItem(int position) {
        return wallets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return wallets.get(position).getId();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Wallet wallet = (Wallet)getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.frag_list_item_wallet,
                    parent,
                    false
            );
        }
        TextView tv_nickname = convertView.findViewById(R.id.tv_wallet_nickname);
        TextView tv_address = convertView.findViewById(R.id.tv_wallet_address);

        if (wallet == null){
            tv_nickname.setText( "" );
            tv_address.setText( "" );
        }
        else {
            tv_nickname.setText( wallet.getNickname() );
            tv_address.setText( wallet.getAddress() );
        }
        return convertView;
    }
}
