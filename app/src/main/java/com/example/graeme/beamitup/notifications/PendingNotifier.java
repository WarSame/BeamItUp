package com.example.graeme.beamitup.notifications;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.example.graeme.beamitup.listener.TransferState;

import org.web3j.protocol.core.methods.response.Transaction;

import java.util.Map;

public class PendingNotifier extends TransferNotifier {
    private static final String TAG = "PendingNotifier";
    private static final String PENDING_TRANSACTION_TITLE = "Incoming pending transaction";
    private static final int PENDING_TRANSACTION_COLOR = Color.YELLOW;
    private static final long[] PENDING_TRANSACTION_PATTERN = new long[]{0, 100, 100};

    public PendingNotifier(
            Context context,
            Transaction tx,
            Map<String, TransferState> notifications
    ) {
        super(context, tx, notifications);
    }

    @Override
    public void on_transfer(PostTransfer postTransfer) {
        boolean transaction_exists = this.notifications.containsKey(tx.getHash());
        if (transaction_exists){
            Log.d(TAG, "Transaction already exists in notification");
            return;
        }
        Log.d(TAG, "Adding pending notification");
        this.notifications.put(tx.getHash(), TransferState.PENDING);
        send_notification();
        postTransfer.post_transfer();
    }

    @Override
    String getTitle() {
        return PENDING_TRANSACTION_TITLE;
    }

    @Override
    int getColor() {
        return PENDING_TRANSACTION_COLOR;
    }

    @Override
    long[] getPattern() {
        return PENDING_TRANSACTION_PATTERN;
    }
}
