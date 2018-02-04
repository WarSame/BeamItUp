package com.example.graeme.beamitup.wallet;

public class EncryptedWallet {
    private byte[] encryptedLongPassword;
    private byte[] IV;

    public EncryptedWallet(byte[] encryptedLongPassword, byte[] IV){
        this.encryptedLongPassword = encryptedLongPassword;
        this.IV = IV;
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

}
