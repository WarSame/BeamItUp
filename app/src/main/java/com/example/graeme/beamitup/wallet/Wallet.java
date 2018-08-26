package com.example.graeme.beamitup.wallet;

import android.content.Context;
import android.util.Log;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import encryption.Decryptor;

@Entity
public class Wallet implements Serializable {
    private static final long serialVersionUID = -4347720392132901969L;
    private static final String TAG = "Wallet";
    private static final String WALLET_DIR_RELATIVE_PATH = "/wallets";

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


    public static Credentials retrieveCredentials(Wallet wallet, File walletFile) throws Exception {
        Log.i(TAG, "Wallet file location: " + walletFile);
        String longPassword = new Decryptor.DecryptorBuilder()
                .setWalletName(wallet.getWalletName())
                .setIV(wallet.getIV())
                .setEncryptedLongPassword(wallet.getEncryptedLongPassword())
                .build()
                .decryptWalletPassword();
        Log.i(TAG, "Wallet retrieved");
        return WalletUtils.loadCredentials(longPassword, walletFile);
    }

    public static Credentials retrieveCredentials(File walletFile, String longPassword) throws Exception{
        Log.i(TAG, "Wallet file location: " + walletFile);
        Credentials credentials = WalletUtils.loadCredentials(longPassword, walletFile);
        Log.i(TAG, "Wallet retrieved");
        return credentials;
    }

    public static String generateWallet(String longPassword, File walletDir) throws Exception {
        return WalletUtils.generateLightNewWalletFile(longPassword, walletDir);
    }

    public static File getWalletFile(Context context, String walletName) throws Exception {
        return new File(getWalletDir(context) + "/" + walletName);
    }

    public static File getWalletDir(Context context) throws IOException {
        File walletDir = new File(context.getFilesDir() + WALLET_DIR_RELATIVE_PATH);
        if (!walletDir.exists()){
            if ( !walletDir.mkdir() ){
                throw new IOException();
            }
        }
        return walletDir;
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
