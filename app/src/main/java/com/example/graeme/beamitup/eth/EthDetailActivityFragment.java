package com.example.graeme.beamitup.eth;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.graeme.beamitup.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class EthDetailActivityFragment extends Fragment {

    public EthDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_eth_detail, container, false);
    }
}
