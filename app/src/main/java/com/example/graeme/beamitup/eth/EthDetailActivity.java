package com.example.graeme.beamitup.eth;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graeme.beamitup.BeamItUp;
import com.example.graeme.beamitup.QRCodeDisplayFragment;
import com.example.graeme.beamitup.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class EthDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eth_detail);

        EditText et_eth_nickname = findViewById(R.id.et_eth_nickname);

        Eth eth = (Eth) getIntent().getSerializableExtra("eth");
        et_eth_nickname.setText(eth.getNickname());

        Fragment qrCodeDisplayFragment = QRCodeDisplayFragment.newInstance(eth.getAddress());
        getFragmentManager()
                .beginTransaction()
                .add(qrCodeDisplayFragment, "QRCodeDisplayFragment")
                .commit();

        Button btn_save_eth = findViewById(R.id.btn_save_eth);

        btn_save_eth.setOnClickListener((v)->{
            eth.setNickname(et_eth_nickname.getText().toString());
            updateEth(eth);
        });
    }

    private void updateEth(Eth eth) {
        DaoSession daoSession = ((BeamItUp) getApplication()).getDaoSession();
        EthDao ethDao = daoSession.getEthDao();
        ethDao.update(eth);
        Toast.makeText(this, "Eth saved", Toast.LENGTH_LONG).show();

        Intent ethListIntent = new Intent(this, EthListActivity.class);
        startActivity(ethListIntent);
    }

}
