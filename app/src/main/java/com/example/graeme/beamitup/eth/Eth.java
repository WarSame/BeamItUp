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

    public Eth setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public Eth setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getWalletName() {
        return walletName;
    }

    public Eth setWalletName(String walletName) {
        this.walletName = walletName;
        return this;
    }

    public byte[] getEncryptedLongPassword() {
        return encryptedLongPassword;
    }

    public Eth setEncryptedLongPassword(byte[] encryptedLongPassword) {
        this.encryptedLongPassword = encryptedLongPassword;
        return this;
    }

    public byte[] getIV() {
        return IV;
    }

    public Eth setIV(byte[] IV) {
        this.IV = IV;
        return this;
    }

    public Long getId() {
        return this.id;
    }

    public Eth setId(Long id) {
        this.id = id;
        return this;
    }
}
