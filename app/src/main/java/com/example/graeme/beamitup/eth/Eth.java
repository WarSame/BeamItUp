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

    private Eth(EthBuilder ethBuilder){
        this.nickname = ethBuilder.getNickname();
        this.address = ethBuilder.getAddress();
        this.walletName = ethBuilder.getWalletName();
        this.encryptedLongPassword = ethBuilder.getEncryptedLongPassword();
        this.IV = ethBuilder.getIV();
    }

    @Generated(hash = 1727113266)
    public Eth() {
    }

    public String getNickname() {
        return nickname;
    }

    public String getAddress() {
        return address;
    }

    public String getWalletName() {
        return walletName;
    }

    public byte[] getEncryptedLongPassword() {
        return encryptedLongPassword;
    }

    public byte[] getIV() {
        return IV;
    }

    public Long getId() {
        return this.id;
    }

    public Eth setId(Long id) {
        this.id = id;
        return this;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public void setEncryptedLongPassword(byte[] encryptedLongPassword) {
        this.encryptedLongPassword = encryptedLongPassword;
    }

    public void setIV(byte[] IV) {
        this.IV = IV;
    }

    public static class EthBuilder{
        private String nickname;
        private String address;
        private String walletName;
        private byte[] encryptedLongPassword;
        private byte[] IV;

        public EthBuilder(){}

        public Eth build(){
            return new Eth(this);
        }

        String getNickname() {
            return nickname;
        }

        String getAddress() {
            return address;
        }

        String getWalletName() {
            return walletName;
        }

        byte[] getEncryptedLongPassword() {
            return encryptedLongPassword;
        }

        byte[] getIV() {
            return IV;
        }

        public EthBuilder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public EthBuilder address(String address) {
            this.address = address;
            return this;
        }

        public EthBuilder walletName(String walletName) {
            this.walletName = walletName;
            return this;
        }

        public EthBuilder encryptedLongPassword(byte[] encryptedLongPassword) {
            this.encryptedLongPassword = encryptedLongPassword;
            return this;
        }

        public EthBuilder IV(byte[] IV) {
            this.IV = IV;
            return this;
        }
    }
}
