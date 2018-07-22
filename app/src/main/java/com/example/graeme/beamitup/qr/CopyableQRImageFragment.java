package com.example.graeme.beamitup.qr;

import android.app.Fragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.graeme.beamitup.Copyable;
import com.example.graeme.beamitup.R;
import com.google.zxing.WriterException;

public class CopyableQRImageFragment extends Fragment implements Copyable {
    private static final String ARG_WALLET_ADDRESS = "wallet_address";

    public CopyableQRImageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param walletAddress - string to copy
     * @return A new instance of fragment CopyableQRImageFragment.
     */
    public static CopyableQRImageFragment newInstance(String walletAddress) {
        CopyableQRImageFragment fragment = new CopyableQRImageFragment();
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

        String address = getArguments().getString(ARG_WALLET_ADDRESS);

        ImageView qr = getActivity().findViewById(R.id.iv_qr_code);
        QRImage qrImage = new QRImage(address);
        try {
            qr.setImageBitmap(qrImage.generateQRImage());
        } catch (WriterException e) {
            e.printStackTrace();
        }

        qr.setOnClickListener(
                v -> copy("Wallet Address", qrImage.getAddress())
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_copyable_qrcode, container, false);
    }

    @Override
    public void copy(String label, String data) {
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, data);
        if (clipboard == null || clip == null){
            return;
        }
        clipboard.setPrimaryClip(clip);
    }
}
