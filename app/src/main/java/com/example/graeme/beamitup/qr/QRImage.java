package com.example.graeme.beamitup.qr;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;

import com.example.graeme.beamitup.Copyable;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.Optional;

public class QRImage implements Copyable {
    private String label;
    private String data;
    private Bitmap bitmap;

    private QRImage(QRImageBuilder qrImageBuilder) throws WriterException {
        this.data = qrImageBuilder.getData();
        this.label = qrImageBuilder.getLabel();
        this.bitmap = generateQRImage();
    }

    @Override
    public void copy() {
    }

    private Bitmap generateQRImage() throws WriterException{
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 200, 200);
        return new BarcodeEncoder().createBitmap(bitMatrix);
    }

    static class QRImageBuilder {
        private String data;
        private String label;
        private ClipboardManager clipboardManager;

        QRImageBuilder(){}

        public QRImageBuilder data(String data){
            this.data = data;
            return this;
        }

        public String getData(){
            return this.data;
        }

        public QRImageBuilder label(String label){
            this.label = label;
            return this;
        }

        public String getLabel(){
            return this.label;
        }

        public QRImageBuilder clipboardManager(ClipboardManager clipboardManager){
            this.clipboardManager = clipboardManager;
            return this;
        }

        public ClipboardManager getClipboardManager(){
            return this.clipboardManager;
        }

        public Optional<QRImage> build(){
            try {
                QRImage qrImage = new QRImage(this);
                return Optional.of(qrImage);
            } catch (WriterException e) {
                e.printStackTrace();
                return Optional.empty();
            }
        }
    }
}
