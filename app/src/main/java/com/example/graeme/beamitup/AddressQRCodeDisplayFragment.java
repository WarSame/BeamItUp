package com.example.graeme.beamitup;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class AddressQRCodeDisplayFragment extends Fragment {
    private static final String ARG_WALLET_ADDRESS = "walletAddress";

    private String walletAddress;

    public AddressQRCodeDisplayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param walletAddress wallet address of wallet
     * @return A new instance of fragment AddressQRCodeDisplayFragment.
     */
    public static AddressQRCodeDisplayFragment newInstance(String walletAddress) {
        AddressQRCodeDisplayFragment fragment = new AddressQRCodeDisplayFragment();
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
        ImageView iv_qr_code = getActivity().findViewById(R.id.iv_qr_code);
        createQRImage(iv_qr_code);
        iv_qr_code.setOnClickListener((v)->{
            ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Wallet address", walletAddress);
            if (clipboard == null || clip == null){
                return;
            }
            clipboard.setPrimaryClip(clip);
        });
    }

    private void createQRImage(ImageView iv_qr_code) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(walletAddress, BarcodeFormat.QR_CODE, 200, 200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            iv_qr_code.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_qrcode_display, container, false);
    }

}
