package com.example.graeme.beamitup.notifier;

import android.content.Context;
import android.graphics.Color;

import com.example.graeme.beamitup.listener.TransferState;

import org.web3j.protocol.core.methods.response.Transaction;

import java.util.Map;

public class PendingNotifier extends TransferNotifier {
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
    public void on_transaction() {
        boolean transaction_exists = this.notifications.containsKey(tx.getHash());
        if (transaction_exists){
            return;
        }
        this.notifications.put(tx.getHash(), TransferState.PENDING);
        send_notification();
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
