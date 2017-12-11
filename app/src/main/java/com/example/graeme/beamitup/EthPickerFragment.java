package com.example.graeme.beamitup;

import android.app.ListFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class EthPickerFragment extends ListFragment {
    private static final String TAG = "EthPickerFragment";

    ArrayList<Eth> eths;

    public EthPickerFragment(){
        eths = Session.getUserDetails().getEths();
    }

    @Override
    public void onListItemClick(ListView lv, View v, int position, long id){
        Log.d(TAG, "clicked item");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        EthPickerAdapter adapter = new EthPickerAdapter(getActivity(), eths);
        setListAdapter(adapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

}
