package com.example.graeme.beamitup.eth;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Keep;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Eth implements Serializable {
    private static final long serialVersionUID = -4347720392132901969L;

    private String nickname;
    private String address;
    private String walletName;
    private byte[] encryptedLongPassword;
    private byte[] IV;

    @Id(autoincrement = true)
    private Long id;

    @Keep
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

    @Keep
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
    }

    @Generated(hash = 1393368730)
    public Eth(String nickname, String address, String walletName,
            byte[] encryptedLongPassword, byte[] IV, Long id) {
        this.nickname = nickname;
        this.address = address;
        this.walletName = walletName;
        this.encryptedLongPassword = encryptedLongPassword;
        this.IV = IV;
        this.id = id;
    }

    @Generated(hash = 1727113266)
    public Eth() {
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

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
