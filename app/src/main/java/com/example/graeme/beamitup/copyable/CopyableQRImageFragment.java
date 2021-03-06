package com.example.graeme.beamitup.copyable;

import android.app.Fragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.graeme.beamitup.copyable.Copyable;
import com.example.graeme.beamitup.R;
import com.example.graeme.beamitup.qr.QRImage;
import com.example.graeme.beamitup.wallet.WalletDetailActivity;
import com.google.zxing.WriterException;

public class CopyableQRImageFragment extends Fragment implements Copyable {
    private static final String TAG = "CopyableQRImageFragment";

    public CopyableQRImageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_copyable_qrcode, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        String address = ((WalletDetailActivity)getActivity()).getAddress();
        ImageView iv_qr = view.findViewById(R.id.iv_qr_code);
        QRImage qrImage = new QRImage(address);
        try {
            Bitmap bitmap = qrImage.generateQRImage();
            iv_qr.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        iv_qr.setOnClickListener(
            v -> copy("Wallet Address", qrImage.getAddress())
        );
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
