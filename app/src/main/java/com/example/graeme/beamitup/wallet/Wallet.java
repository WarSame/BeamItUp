package com.example.graeme.beamitup.wallet;

public class Wallet {
    private String walletName;
    private String longPassword;

    public Wallet(String walletName, String longPassword) {
        this.walletName = walletName;
        this.longPassword = longPassword;
    }

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public String getLongPassword() {
        return longPassword;
    }

    public void setLongPassword(String longPassword) {
        this.longPassword = longPassword;
    }
}
