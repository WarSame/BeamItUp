package com.example.graeme.beamitup.wallet;

public class EncryptedWallet {
    private byte[] encryptedLongPassword;
    private byte[] IV;
    private String walletName;
    private long id;

    public EncryptedWallet(byte[] encryptedLongPassword, byte[] IV, String walletName){
        this.encryptedLongPassword = encryptedLongPassword;
        this.IV = IV;
        this.walletName = walletName;
        this.id = -1;
    }

    public EncryptedWallet(byte[] encryptedLongPassword, byte[] IV, String walletName, long id){
        this.encryptedLongPassword = encryptedLongPassword;
        this.IV = IV;
        this.walletName = walletName;
        this.id = id;
    }

    public byte[] getEncryptedLongPassword() {
        return encryptedLongPassword;
    }

    public void setEncryptedLongPassword(byte[] encryptedLongPassword) {
        this.encryptedLongPassword = encryptedLongPassword;
    }

    public byte[] getIV() {
        return IV;
    }

    public void setIV(byte[] IV) {
        this.IV = IV;
    }

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
