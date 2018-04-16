package com.example.graeme.beamitup.eth;

import java.io.Serializable;

public class Eth implements Serializable{
    private String nickname;
    private String address;
    private String walletName;
    private byte[] encryptedLongPassword;
    private byte[] IV;
    private long id;

    public Eth(
            String nickname,
            String address,
            String walletName,
            byte[] encryptedLongPassword,
            byte[] IV,
            long id
            ){
        this.nickname = nickname;
        this.address = address;
        this.walletName = walletName;
        this.encryptedLongPassword = encryptedLongPassword;
        this.IV = IV;
        this.id = id;
    }

    public Eth(
            String nickname,
            String address,
            String walletName,
            byte[] encryptedLongPassword,
            byte[] IV
    ){
        this.nickname = nickname;
        this.address = address;
        this.walletName = walletName;
        this.encryptedLongPassword = encryptedLongPassword;
        this.IV = IV;
        this.id = -1;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
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
