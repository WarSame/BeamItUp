package com.example.graeme.beamitup;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

class Encryption {
    private static final String TAG = "Encryption";
    private static final String HASHING_ALGORITHM = "SHA-256";
    private static final String KEY_PROVIDER = "AndroidKeyStore";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final String TEXT_FORMAT = "UTF-8";

    static byte[] hashPassword(String password, byte[] salt) throws NoSuchAlgorithmException{
        MessageDigest md;
        byte[] passwordHash;

        md = MessageDigest.getInstance(HASHING_ALGORITHM);
        md.reset();
        md.update(salt);
        passwordHash = md.digest(password.getBytes());

        if (passwordHash == null){
            return null;
        }

        return passwordHash;
    }

    static byte[] generateSalt(){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[20];
        random.nextBytes(salt);
        return salt;
    }

    static class Encryptor {
        private byte[] encryption;
        private byte[] iv;

        Encryptor(){
        }

        void encryptPrivateKey(String ethAddress, String privateKeyString) throws Exception {
            this.encryptText(ethAddress, privateKeyString);
        }

        void encryptText(final String alias, final String textToEncrypt) throws Exception {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(alias));

            this.iv = cipher.getIV();
            this.encryption = cipher.doFinal(textToEncrypt.getBytes(TEXT_FORMAT));
        }

        @NonNull
        private SecretKey getSecretKey(final String alias) throws NoSuchAlgorithmException,
                NoSuchProviderException, InvalidAlgorithmParameterException {

            final KeyGenerator keyGenerator = KeyGenerator
                    .getInstance(KeyProperties.KEY_ALGORITHM_AES, KEY_PROVIDER);

            keyGenerator.init(new KeyGenParameterSpec.Builder(alias,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build());

            return keyGenerator.generateKey();
        }

        byte[] getEncryption(){
            return this.encryption;
        }

        void setEncryption(byte[] encryption){
            this.encryption = encryption;
        }

        byte[] getIv(){
            return this.iv;
        }

        void setIv(byte[] iv){
            this.iv = iv;
        }
    }

    static class Decryptor {
        private KeyStore ks;

        Decryptor() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
            ks = KeyStore.getInstance(KEY_PROVIDER);
            ks.load(null);
        }

        String decryptPrivateKey(String ethAddress, byte[] privateKeyEnc, byte[] iv) throws Exception {
            return this.decryptText(ethAddress, privateKeyEnc, iv);
        }

        String decryptText(final String alias, final byte[] encryptedData, final byte[] encryptionIV) throws Exception {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec spec = new GCMParameterSpec(128, encryptionIV);
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(alias), spec);
            return new String(cipher.doFinal(encryptedData), TEXT_FORMAT);
        }

        private SecretKey getSecretKey(final String alias) throws UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException {
            return ((KeyStore.SecretKeyEntry) ks.getEntry(alias, null)).getSecretKey();
        }
    }
}
