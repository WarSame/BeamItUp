package com.example.graeme.beamitup.qr;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.graeme.beamitup.Copyable;

public class AbstractCopyableFragment extends Fragment implements Copyable {
    private static final String ARG_FRAG_ID = "frag_id";
    private static final String ARG_LABEL = "label";
    private static final String ARG_DATA = "data";
    private static final String ARG_VIEW_ID = "view_id";

    public AbstractCopyableFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param data string to copy
     * @return A new instance of fragment AbstractCopyableFragment.
     */
    public static AbstractCopyableFragment newInstance(int fragID, String data, String label, int viewID) {
        AbstractCopyableFragment fragment = new AbstractCopyableFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_FRAG_ID, fragID);
        args.putString(ARG_DATA, data);
        args.putString(ARG_LABEL, label);
        args.putInt(ARG_VIEW_ID, viewID);
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

        int fragID = getArguments().getInt(ARG_FRAG_ID);
        String data = getArguments().getString(ARG_DATA);
        String label = getArguments().getString(ARG_LABEL);
        int viewID = getArguments().getInt(ARG_VIEW_ID);

        View view = getActivity().findViewById(viewID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(getArguments().getInt(ARG_FRAG_ID), container, false);
    }

    @Override
    public void copy() {
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, data);
        if (clipboard == null || clip == null){
            return;
        }
        clipboard.setPrimaryClip(clip);
    }
}
