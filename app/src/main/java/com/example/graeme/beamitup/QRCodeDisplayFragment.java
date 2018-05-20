package com.example.graeme.beamitup;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QRCodeDisplayFragment extends Fragment {
    private static final String ARG_ETH_ADDRESS = "ethAddress";

    private String ethAddress;

    public QRCodeDisplayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param ethAddress eth address of wallet
     * @return A new instance of fragment QRCodeDisplayFragment.
     */
    public static QRCodeDisplayFragment newInstance(String ethAddress) {
        QRCodeDisplayFragment fragment = new QRCodeDisplayFragment();
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
        else {
            ethAddress = getArguments().getString(ARG_ETH_ADDRESS);
        }
        if (ethAddress == null){
            return;
        }

        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(ethAddress, BarcodeFormat.QR_CODE, 200, 200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            ImageView qr_code = getActivity().findViewById(R.id.iv_qr_code);
            qr_code.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_qrcode_display, container, false);
    }

}
