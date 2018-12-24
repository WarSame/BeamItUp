package com.example.graeme.beamitup.listener;

import org.web3j.protocol.websocket.WebSocketListener;

public interface TransferListener extends WebSocketListener {
    void onMessage(String message);
}