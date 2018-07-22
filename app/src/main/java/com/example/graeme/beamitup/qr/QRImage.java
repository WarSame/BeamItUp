package com.example.graeme.beamitup.qr;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.journeyapps.barcodescanner.BarcodeEncoder;


public class QRImage {
    private String address;

    public QRImage(String address) {
        this.address = address;
    }

    public String getAddress(){
        return this.address;
    }

    public Bitmap generateQRImage() throws WriterException{
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(this.address, BarcodeFormat.QR_CODE, 200, 200);
        return new BarcodeEncoder().createBitmap(bitMatrix);
    }
}
