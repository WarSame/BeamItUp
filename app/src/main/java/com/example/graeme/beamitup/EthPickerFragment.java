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
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class EthPickerFragment extends ListFragment {
    private static final String TAG = "EthPickerFragment";

    ArrayList<Eth> eths;
    onEthSelectedListener ethListener;

    public EthPickerFragment(){
        eths = Session.getUserDetails().getEths();
    }

    @Override
    public void onListItemClick(ListView lv, View v, int position, long id){
        ethListener.onEthSelected(eths.get(position));
        getListView().setItemChecked(position, true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        EthPickerAdapter adapter = new EthPickerAdapter(getActivity(), eths);
        setListAdapter(adapter);
        getListView().setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        getListView().setSelector(R.color.colorAccent);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            ethListener = (onEthSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnArticleSelectedListener");
        }
    }

    public interface onEthSelectedListener {
        void onEthSelected(Eth eth);
    }

}