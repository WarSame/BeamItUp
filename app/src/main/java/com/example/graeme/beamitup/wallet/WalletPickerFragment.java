package com.example.graeme.beamitup.wallet;

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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;


public class WalletPickerFragment extends ListFragment {
    private static final String TAG = "WalletPickerFragment";

    List<Wallet> wallets;
    onWalletSelectedListener walletListener;
    WalletPickerAdapter adapter;

    @Override
    public void onListItemClick(ListView lv, View v, int position, long id){
        walletListener.onWalletSelected(wallets.get(position));
        getListView().setItemChecked(position, true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        adapter = new WalletPickerAdapter(getActivity(), wallets);
        setListAdapter(adapter);
        ListView lv_walletPicker = getListView();
        lv_walletPicker.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        lv_walletPicker.setSelector(R.color.colorAccent);
        lv_walletPicker.setBackground(getResources().getDrawable(R.drawable.pickerback, null));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        DaoSession daoSession = ((BeamItUp)getActivity().getApplication()).getDaoSession();
        WalletDao walletDao = daoSession.getWalletDao();

        wallets = walletDao.loadAll();
        
        try {
            walletListener = (onWalletSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnArticleSelectedListener");
        }
    }

    public interface onWalletSelectedListener {
        void onWalletSelected(Wallet wallet);
    }

    @Override
    public void onStart(){
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop(){
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleWalletEvent(WalletEvent walletEvent){
        Wallet wallet = walletEvent.getWallet();
        wallets.add(wallet);
        adapter.notifyDataSetChanged();
    }

}
