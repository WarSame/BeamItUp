package com.example.graeme.beamitup.listener;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.graeme.beamitup.R;

import org.web3j.protocol.core.methods.response.Transaction;

public class TransactionNotification {
    private static final String TAG = "TransactionNotification";

    private Context context;
    private Transaction tx;
    private String hash;
    private State state;

    private static final long[] SUCCESS_VIBRATE_PATTERN = new long[]{0, 200, 100, 200};
    private static final long[] FAILURE_VIBRATE_PATTERN = new long[]{0, 50, 25, 50};
    private static final long[] START_VIBRATE_PATTERN = new long[]{0, 100};

    private static final long[] PENDING_TRANSACTION_PATTERN = new long[]{0, 100, 100};
    private static final long[] BLOCK_TRANSACTION_PATTERN = new long[]{0, 200, 200};

    private static final int PENDING_TRANSACTION_COLOR = Color.YELLOW;
    private static final int BLOCK_TRANSACTION_COLOR = Color.GREEN;

    public enum State {
        PENDING,
        IN_BLOCK,
        CONFIRMED
    }

    TransactionNotification(
            Context context,
            Transaction tx
    ){
        this.context = context;
        this.tx = tx;
        this.state = State.PENDING;
        this.hash = tx.getHash();
    }

    public void notify_pend_tx(){
        if (this.state != State.PENDING){
            return;
        }
        notify(
                "Incoming pending transaction",
                this.tx.getValue()+ " WEI from " + tx.getFrom(),
                PENDING_TRANSACTION_COLOR,
                PENDING_TRANSACTION_PATTERN
        );
    }

    public void notify_block_tx(){
        notify(
                "Incoming transaction in block",
                this.tx.getValue() + " WEI from " + tx.getFrom(),
                BLOCK_TRANSACTION_COLOR,
                BLOCK_TRANSACTION_PATTERN
        );
    }

    private void notify(
            String title,
            String text,
            int color,
            long[] pattern
    ){
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

        Log.d(TAG, "Hash: " + this.hash);

        notificationManagerCompat.notify(this.hash, 1, builder.build());
    }
}
