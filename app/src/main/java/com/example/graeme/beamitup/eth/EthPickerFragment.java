package com.example.graeme.beamitup.eth;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.graeme.beamitup.BeamItUp;
import com.example.graeme.beamitup.R;

import java.util.List;


public class EthPickerFragment extends ListFragment {
    private static final String TAG = "EthPickerFragment";

    List<Eth> eths;
    onEthSelectedListener ethListener;

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
        ListView lv_ethPicker = getListView();
        lv_ethPicker.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        lv_ethPicker.setSelector(R.color.colorAccent);
        lv_ethPicker.setBackground(getResources().getDrawable(R.drawable.pickerback, null));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        DaoSession daoSession = ((BeamItUp)getActivity().getApplication()).getDaoSession();
        EthDao ethDao = daoSession.getEthDao();

        eths = ethDao.loadAll();
        
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
