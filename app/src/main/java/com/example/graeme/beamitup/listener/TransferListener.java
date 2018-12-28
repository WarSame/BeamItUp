package com.example.graeme.beamitup.listener;

import org.web3j.protocol.core.methods.response.Transaction;

public interface TransferListener {
    void onMessage(Transaction tx);
}