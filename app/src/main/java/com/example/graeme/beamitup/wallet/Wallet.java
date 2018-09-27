package com.example.graeme.beamitup.wallet;

import android.content.Context;
import android.util.Log;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import com.example.graeme.beamitup.encryption.Decryptor;
import com.example.graeme.beamitup.encryption.Encryption;
import com.example.graeme.beamitup.encryption.Encryptor;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Wallet implements Serializable {
    private static final long serialVersionUID = -4347720392132901969L;
    private static final String TAG = "Wallet";
    public static final String WALLET_DIR_RELATIVE_PATH = "/wallets";

    private String nickname;
    private String address;
    private String fileName;
    private byte[] encryptedLongPassword;
    private byte[] IV;
    private String location;

    @Id(autoincrement = true)
    private Long id;

    private Wallet(WalletBuilder walletBuilder) throws Exception{
        this.nickname = walletBuilder.getNickname();
        Context context = walletBuilder.getContext();
        boolean isUserAuthenticationRequired = walletBuilder.isUserAuthenticationRequired;

        String longPassword = Encryption.generateLongRandomString();
        this.fileName = WalletUtils.generateLightNewWalletFile(longPassword, getWalletDir(context));
        Encryptor encryptor = new Encryptor()
                .setUserAuthenticationRequired(isUserAuthenticationRequired)
                .encryptWalletPassword(fileName, longPassword);
        this.IV = encryptor.getIV();
        this.encryptedLongPassword = encryptor.getEncryptedLongPassword();
        this.location = getWalletLocation(context, fileName);

        this.address = retrieveCredentials().getAddress();
    }

    @Generated(hash = 1646271601)
    public Wallet(String nickname, String address, String fileName, byte[] encryptedLongPassword,
            byte[] IV, String location, Long id) {
        this.nickname = nickname;
        this.address = address;
        this.fileName = fileName;
        this.encryptedLongPassword = encryptedLongPassword;
        this.IV = IV;
        this.location = location;
        this.id = id;
    }

    @Generated(hash = 1197745249)
    public Wallet() {
    }

    public Credentials retrieveCredentials() throws Exception {
        Log.i(TAG, "Wallet file location: " + location);
        String longPassword = new Decryptor.DecryptorBuilder()
                .setWalletName(this.fileName)
                .setIV(this.IV)
                .setEncryptedLongPassword(this.encryptedLongPassword)
                .build()
                .decryptWalletPassword();
        Log.i(TAG, "Wallet retrieved");
        return WalletUtils.loadCredentials(longPassword, location);
    }

    private String getWalletLocation(Context context, String walletName) throws IOException {
        return getWalletDir(context) + "/" + walletName;
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

    public String getNickname() {
        return nickname;
    }

    public String getAddress() {
        return address;
    }

    public String getFileName() {
        return fileName;
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

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setEncryptedLongPassword(byte[] encryptedLongPassword) {
        this.encryptedLongPassword = encryptedLongPassword;
    }

    public void setIV(byte[] IV) {
        this.IV = IV;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public static class WalletBuilder{
        private String nickname;
        private Context context;
        private boolean isUserAuthenticationRequired = true;

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

        public WalletBuilder isUserAuthenticationRequired(boolean isUserAuthenticationRequired) {
            this.isUserAuthenticationRequired = isUserAuthenticationRequired;
            return this;
        }
    }
}
