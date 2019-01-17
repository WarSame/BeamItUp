package com.example.graeme.beamitup.notifier;

import android.content.Context;
import android.graphics.Color;

import com.example.graeme.beamitup.listener.TransferState;

import org.web3j.protocol.core.methods.response.Transaction;

import java.util.Map;

public class BlockNotifier extends TransferNotifier {
    private static final String BLOCK_TRANSACTION_TITLE = "Incoming block transaction";
    private static final long[] BLOCK_TRANSACTION_PATTERN = new long[]{0, 200, 200};
    private static final int BLOCK_TRANSACTION_COLOR = Color.GREEN;

    public BlockNotifier(
            Context context,
            Transaction tx,
            Map<String, TransferState> notifications
    ) {
        super(context, tx, notifications);
    }

    @Override
    public void on_transfer(PostTransfer postTransfer) {
        this.notifications.put(tx.getHash(), TransferState.IN_BLOCK);
        send_notification();
        postTransfer.post_transfer();
    }

    @Override
    String getTitle() {
        return BLOCK_TRANSACTION_TITLE;
    }

    @Override
    int getColor() {
        return BLOCK_TRANSACTION_COLOR;
    }

    @Override
    long[] getPattern() {
        return BLOCK_TRANSACTION_PATTERN;
    }
}
