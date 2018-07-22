package com.example.graeme.beamitup.wallet;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Wallet implements Serializable {
    private static final long serialVersionUID = -4347720392132901969L;

    private String nickname;
    private String address;
    private String walletName;
    private byte[] encryptedLongPassword;
    private byte[] IV;

    @Id(autoincrement = true)
    private Long id;

    @Generated(hash = 1077495790)
    public Wallet(String nickname, String address, String walletName,
            byte[] encryptedLongPassword, byte[] IV, Long id) {
        this.nickname = nickname;
        this.address = address;
        this.walletName = walletName;
        this.encryptedLongPassword = encryptedLongPassword;
        this.IV = IV;
        this.id = id;
    }

    private Wallet(WalletBuilder walletBuilder){
        this.nickname = walletBuilder.getNickname();
        this.address = walletBuilder.getAddress();
        this.walletName = walletBuilder.getWalletName();
        this.encryptedLongPassword = walletBuilder.getEncryptedLongPassword();
        this.IV = walletBuilder.getIV();
    }

    @Generated(hash = 1197745249)
    public Wallet() {
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

    public Wallet setId(Long id) {
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

    public static class WalletBuilder{
        private String nickname;
        private String address;
        private String walletName;
        private byte[] encryptedLongPassword;
        private byte[] IV;

        public WalletBuilder(){}

        public Wallet build(){
            return new Wallet(this);
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

        public WalletBuilder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public WalletBuilder address(String address) {
            this.address = address;
            return this;
        }

        public WalletBuilder walletName(String walletName) {
            this.walletName = walletName;
            return this;
        }

        public WalletBuilder encryptedLongPassword(byte[] encryptedLongPassword) {
            this.encryptedLongPassword = encryptedLongPassword;
            return this;
        }

        public WalletBuilder IV(byte[] IV) {
            this.IV = IV;
            return this;
        }
    }
}
