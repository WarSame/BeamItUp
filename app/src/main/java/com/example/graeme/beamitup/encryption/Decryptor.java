package com.example.graeme.beamitup.encryption;


import android.util.Log;

import java.security.Key;
import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

//Retrieve encrypted long password from DB
//Retrieve key with walletName alias
//Decrypt long password with key
public class Decryptor {
    private static final String TAG = "Decryptor";
    private byte[] encryptedLongPassword;
    private byte[] IV;
    private String walletName;

    private Decryptor(
            DecryptorBuilder decryptorBuilder
    ){
        this.encryptedLongPassword = decryptorBuilder.encryptedLongPassword;
        this.IV = decryptorBuilder.IV;
        this.walletName = decryptorBuilder.walletName;
    }

    public static class DecryptorBuilder{
        private byte[] encryptedLongPassword;
        private byte[] IV;
        private String walletName;

        public DecryptorBuilder(){
        }

        public Decryptor build(){
            return new Decryptor(
                    this
            );
        }

        public DecryptorBuilder setEncryptedLongPassword(byte[] encryptedLongPassword) {
            this.encryptedLongPassword = encryptedLongPassword;
            return this;
        }

        public DecryptorBuilder setIV(byte[] IV) {
            this.IV = IV;
            return this;
        }

        public DecryptorBuilder setWalletName(String walletName) {
            this.walletName = walletName;
            return this;
        }
    }

    public String decryptWalletPassword() throws Exception {
        Log.i(TAG, "Decrypting wallet password for " + walletName);
        SecretKey aesKey = retrieveKeyFromKeyStore();
        byte[] decryptedLongPassword = decryptAES(aesKey);
        Log.i(TAG, "Decrypted wallet password for " + walletName);
        return new String(decryptedLongPassword);
    }

    private SecretKey retrieveKeyFromKeyStore() throws Exception {
        Log.i(TAG, "Retrieving key for " + walletName);
        KeyStore keyStore = KeyStore.getInstance(Encryption.KEYSTORE_PROVIDER);
        keyStore.load(null);
        KeyStore.SecretKeyEntry entry = (KeyStore.SecretKeyEntry) keyStore.getEntry(walletName, null);
        Log.i(TAG, "Retrieved key for " + walletName);
        return entry.getSecretKey();
    }

    private byte[] decryptAES(final Key aesKey) throws Exception {
        Cipher cipher = Cipher.getInstance(Encryption.AES_CIPHER);
        cipher.init(Cipher.DECRYPT_MODE, aesKey, new GCMParameterSpec(Encryption.GCM_TAG_LENGTH, IV));

        return cipher.doFinal(encryptedLongPassword);
    }
}