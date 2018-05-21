package com.example.graeme.beamitup;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class AddressCopyableTextDisplayFragment extends Fragment {
    private static final String ARG_ETH_ADDRESS = "ethAddress";
    private static final int ADDRESS_DISPLAY_LENGTH = 8;
    private String ethAddress;

    public AddressCopyableTextDisplayFragment() {
        // Required empty public constructor
    }

    public static AddressCopyableTextDisplayFragment newInstance(String ethAddress) {
        AddressCopyableTextDisplayFragment fragment = new AddressCopyableTextDisplayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ETH_ADDRESS, ethAddress);
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
        ethAddress = getArguments().getString(ARG_ETH_ADDRESS);
        if (ethAddress == null){
            return;
        }

        TextView tv_eth_address = getActivity().findViewById(R.id.tv_eth_address);
        String formattedAddress = ethAddress.substring(0, ADDRESS_DISPLAY_LENGTH) + "…";
        tv_eth_address.setText(formattedAddress);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_address_copyable_text_display, container, false);
    }
}
