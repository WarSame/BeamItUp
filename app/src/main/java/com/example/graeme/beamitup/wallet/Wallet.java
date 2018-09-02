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
import encryption.Encryption;
import encryption.Encryptor;

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
    private File walletFile;

    @Id(autoincrement = true)
    private Long id;

    private Wallet(WalletBuilder walletBuilder) throws Exception{
        this.nickname = walletBuilder.getNickname();
        Context context = walletBuilder.getContext();

        String longPassword = Encryption.generateLongRandomString();
        this.walletName = WalletUtils.generateLightNewWalletFile(longPassword, getWalletDir(context));
        Encryptor encryptor = new Encryptor()
                .encryptWalletPassword(walletName, longPassword);
        this.IV = encryptor.getIV();
        this.encryptedLongPassword = encryptor.getEncryptedLongPassword();
        this.walletFile = getWalletFile(context, walletName);

        this.address = retrieveCredentials().getAddress();
    }

    public Credentials retrieveCredentials() throws Exception {
        Log.i(TAG, "Wallet file location: " + walletFile);
        String longPassword = new Decryptor.DecryptorBuilder()
                .setWalletName(this.walletName)
                .setIV(this.IV)
                .setEncryptedLongPassword(this.encryptedLongPassword)
                .build()
                .decryptWalletPassword();
        Log.i(TAG, "Wallet retrieved");
        return WalletUtils.loadCredentials(longPassword, walletFile);
    }

    private File getWalletFile(Context context, String walletName) throws IOException {
        return new File(getWalletDir(context) + "/" + walletName);
    }

    private File getWalletDir(Context context) throws IOException {
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
        private Context context;

        public WalletBuilder(){}

        public Wallet build() throws Exception {
            return new Wallet(this);
        }

        String getNickname() {
            return nickname;
        }

        public Context getContext() {
            return context;
        }

        public WalletBuilder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public WalletBuilder context(Context context){
            this.context = context;
            return this;
        }
    }
}
