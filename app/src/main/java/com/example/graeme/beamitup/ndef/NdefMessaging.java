package com.example.graeme.beamitup.ndef;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;

public class NdefMessaging {
    private static final String TAG = "NdefMessaging";

    public static <T extends Serializable> T handlePushMessage(Intent incomingIntent){
        String incomingIntentType = incomingIntent.getType();

        Log.i(TAG, "Receiving message of: " + incomingIntentType);

        if (incomingIntentType==null){
            return null;
        }

        NdefMessage msg = (NdefMessage)incomingIntent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)[0];
        T message = SerializationUtils.deserialize(msg.getRecords()[0].getPayload());

        Log.i(TAG, message.toString());
        return message;
    }

    public static <T extends  Serializable> void preparePushMessage(T t, Activity activity, String mimeType){
        Log.i(TAG, "Preparing message of " + t.toString());

        NfcAdapter mNfcAdapter = NfcAdapter.getDefaultAdapter(activity);

        if (mNfcAdapter == null) {
            Toast.makeText(activity, "NFC is not available", Toast.LENGTH_LONG).show();
            return;
        }

        NdefRecord rec = NdefRecord.createMime(
                mimeType,
                SerializationUtils.serialize(t)
        );
        NdefMessage message = new NdefMessage(rec);

        mNfcAdapter.setNdefPushMessage(message, activity);
    }
}
