package com.example.graeme.beamitup.wallet;

public class WalletEvent {
    private final Wallet wallet;

    WalletEvent(Wallet wallet){
        this.wallet = wallet;
    }

    public Wallet getWallet() {
        return wallet;
    }
}
