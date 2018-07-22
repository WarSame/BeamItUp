package com.example.graeme.beamitup;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class CopyableAddressFragment extends Fragment {
    private static final String ARG_WALLET_ADDRESS = "walletAddress";
    private static final int ADDRESS_DISPLAY_LENGTH = 8;
    private String walletAddress;

    public CopyableAddressFragment() {
        // Required empty public constructor
    }

    public static CopyableAddressFragment newInstance(String walletAddress) {
        CopyableAddressFragment fragment = new CopyableAddressFragment();
        Bundle args = new Bundle();
        args.putString(ARG_WALLET_ADDRESS, walletAddress);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args == null) {
            return;
        }
        walletAddress = getArguments().getString(ARG_WALLET_ADDRESS);
        if (walletAddress == null){
            return;
        }

        TextView tv_wallet_address = getActivity().findViewById(R.id.tv_wallet_address);
        String formattedAddress = walletAddress.substring(0, ADDRESS_DISPLAY_LENGTH) + "…";
        tv_wallet_address.setText(formattedAddress);

        tv_wallet_address.setOnClickListener((v)->{
            ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Wallet address", walletAddress);
            if (clipboard == null || clip == null){
                return;
            }
            clipboard.setPrimaryClip(clip);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_copyable_address, container, false);
    }
}
