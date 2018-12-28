package com.example.graeme.beamitup.listener;

import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.graeme.beamitup.R;

import org.web3j.protocol.core.methods.response.Transaction;

public class TransactionNotification {
    private static final String TAG = "TransactionNotification";

    private String title;
    private String text;
    private long[] pattern;
    private int color;
    private Transaction tx;
    private Context context;

    public TransactionNotification(
            Context context,
            long[] pattern,
            int color,
            Transaction tx
    ){
        this.context = context;
        this.title = "Incoming transaction";
        this.text = tx.getValue() + " WEI from " + tx.getFrom();
        this.pattern = pattern;
        this.color = color;
        this.tx = tx;
    }

    public void send(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                this.context,
                "BeamItUp"
        )
                .setContentTitle(title)
                .setContentText(text)
                .setColor(color)
                .setSmallIcon(R.drawable.ic_beamitup)
                .setVibrate(pattern);

        NotificationManagerCompat notificationManagerCompat =
                NotificationManagerCompat.from(
                        this.context
                );

        String hash = tx.getHash();
        Log.d(TAG, "Hash: " + hash);

        notificationManagerCompat.notify(hash, 1, builder.build());
    }
}
