package com.example.graeme.beamitup.notifications;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationManagerCompat;

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
        new NotificationWrapper(
                new NotificationBuilderProvider(context),
                context.getSystemService(NotificationManager.class)
        ).postNotification(
                getTitle(),
                getText(),
                getColor(),
                getPattern(),
                tx.getHash()
        );
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