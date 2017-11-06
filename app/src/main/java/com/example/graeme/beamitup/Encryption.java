package com.example.graeme.beamitup;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
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

        byte[] encryptText(final String alias, final String textToEncrypt) throws Exception {
            KeyGenerator kg = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, KEY_PROVIDER);
            kg.init(new KeyGenParameterSpec.Builder(
                    alias,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build()
            );

            Key key = kg.generateKey();
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, key);

            this.iv = cipher.getIV();
            return this.encryption = cipher.doFinal(textToEncrypt.getBytes(TEXT_FORMAT));
        }

        byte[] getEncryption(){
            return this.encryption;
        }

        byte[] getIv(){
            return this.iv;
        }
    }

    static class Decryptor {
        private KeyStore ks;

        Decryptor() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
            ks = KeyStore.getInstance(KEY_PROVIDER);
            ks.load(null);
        }

        String decryptText(final String alias, final byte[] encryptedData, final byte[] encryptionIV) throws Exception {
            KeyStore.Entry entry = ks.getEntry(alias, null);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec spec = new GCMParameterSpec(128, encryptionIV);
            cipher.init(Cipher.DECRYPT_MODE, ((KeyStore.SecretKeyEntry) entry).getSecretKey(), spec);
            return new String(cipher.doFinal(encryptedData), TEXT_FORMAT);
        }
    }
}
