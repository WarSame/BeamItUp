package com.example.graeme.beamitup.notifier;

import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.graeme.beamitup.R;
import com.example.graeme.beamitup.listener.TransferState;

import org.web3j.protocol.core.methods.response.Transaction;

import java.util.Map;

public abstract class TransferNotifier {
    private Context context;
    Transaction tx;
    private static final String TAG = "TransferNotifier";
    Map<String, TransferState> notifications;

    TransferNotifier(
            Context context,
            Transaction tx,
            Map<String, TransferState> notifications
    ){
        this.context = context;
        this.tx = tx;
        this.notifications = notifications;
    }

    public abstract void on_transfer(PostTransfer postTransfer);

    void send_notification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                this.context,
                "BeamItUp"
        )
                .setContentTitle(getTitle())
                .setContentText(getText())
                .setColor(getColor())
                .setSmallIcon(R.drawable.ic_beamitup)
                .setVibrate(getPattern());

        NotificationManagerCompat notificationManagerCompat =
                NotificationManagerCompat.from(
                        this.context
                );

        String hash = this.tx.getHash();
        Log.d(TAG, "Hash: " + hash);

        notificationManagerCompat.notify(hash, 1, builder.build());
    }

    abstract String getTitle();
    String getText(){
        return this.tx.getValue() + " WEI from " + tx.getFrom();
    }
    abstract int getColor();
    abstract long[] getPattern();

    public interface PostTransfer{
        void post_transfer();
    }
}