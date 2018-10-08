package com.example.graeme.beamitup;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.graeme.beamitup.wallet.WalletDetailActivity;


public class CopyableAddressFragment extends Fragment {
    private static final String TAG = "CopyableAddressFragment";
    private String address = "";
    private static final String TAG_ADDRESS = "TAG_ADDRESS";

    public CopyableAddressFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null){
            this.address = savedInstanceState.getString(TAG_ADDRESS);
            Log.i(TAG, "Saved address is " + address);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_copyable_address, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        address = ((WalletDetailActivity)getActivity()).getAddress();
        TextView tv_wallet_address = view.findViewById(R.id.tv_wallet_address);
        tv_wallet_address.setText(address);

        tv_wallet_address.setOnClickListener((v)->{
            ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Wallet address", address);
            if (clipboard == null || clip == null){
                return;
            }
            clipboard.setPrimaryClip(clip);
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(TAG_ADDRESS, address);
    }
}
