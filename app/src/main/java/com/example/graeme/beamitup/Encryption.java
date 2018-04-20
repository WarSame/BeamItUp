package com.example.graeme.beamitup;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;

import com.example.graeme.beamitup.wallet.EncryptedWallet;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class Encryption {
    private static final String TAG = "Encryption";
    private static final String KEYSTORE_PROVIDER = "AndroidKeyStore";

    private static final String AES_CIPHER = KeyProperties.KEY_ALGORITHM_AES + "/" +
            KeyProperties.BLOCK_MODE_GCM + "/" +
            KeyProperties.ENCRYPTION_PADDING_NONE;
    private static final int GCM_TAG_LENGTH = 128;

    private static final int USER_VALIDATION_DURATION_SECONDS = 30;

    private static final int RANDOM_STRING_LENGTH = 128;
    private static final char[] RANDOM_STRING_CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();

    //Generate a long random string as the actual password for the wallet file
    //Generate a key in the keystore with alias walletName
    //Encrypt long password with key
    //Store encrypted blob in DB
    public static EncryptedWallet encryptWalletPassword(String walletName, String longPassword) throws Exception {
        Log.i(TAG, "Encrypting wallet password for " + walletName);
        SecretKey secretKey = generateKey(walletName);
        Cipher cipher = createEncryptionCipher(secretKey);
        byte[] longPasswordBytes = longPassword.getBytes();
        byte[] encryptedLongPassword = cipher.doFinal(longPasswordBytes);
        Log.i(TAG, "Encrypted wallet password for " + walletName);
        return new EncryptedWallet(encryptedLongPassword, cipher.getIV());
    }

    public static String generateLongRandomString(){
        SecureRandom sr = new SecureRandom();
        final int RANDOM_STRING_NUM_CHARS = RANDOM_STRING_CHARACTERS.length;
        StringBuilder randomString = new StringBuilder();
        for (int i = 0; i < RANDOM_STRING_LENGTH; i++){
            int nextIndex = sr.nextInt(RANDOM_STRING_NUM_CHARS);
            char nextChar = RANDOM_STRING_CHARACTERS[ nextIndex ];
            randomString.append( nextChar );
        }
        return randomString.toString();
    }

    private static SecretKey generateKey(String walletName) throws Exception {
        Log.i(TAG, "Generating key for " + walletName);
        KeyGenerator keyGen = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES,
                KEYSTORE_PROVIDER
        );

        KeyGenParameterSpec spec = new KeyGenParameterSpec.Builder(
                walletName,
                KeyProperties.PURPOSE_ENCRYPT
                        | KeyProperties.PURPOSE_DECRYPT
        )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setUserAuthenticationRequired(true)
                .setUserAuthenticationValidityDurationSeconds(USER_VALIDATION_DURATION_SECONDS)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setRandomizedEncryptionRequired(true)
                .build();
        keyGen.init(spec);

        Log.i(TAG, "Generated key for " + walletName);

        return keyGen.generateKey();
    }

    private static Cipher createEncryptionCipher(final Key aesKey) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_CIPHER);
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        return cipher;
    }

    //Retrieve encrypted long password from DB
    //Retrieve key with walletName alias
    //Decrypt long password with key
    public static String decryptWalletPassword(byte[] encryptedLongPassword, byte[] IV, String walletName) throws Exception{
        Log.i(TAG, "Decrypting wallet password for " + walletName);
        SecretKey aesKey = retrieveKeyFromKeyStore(walletName);
        byte[] decryptedLongPassword = decryptAES(encryptedLongPassword, IV, aesKey);
        Log.i(TAG, "Decryped wallet password for " + walletName);
        return new String(decryptedLongPassword);
    }

    private static SecretKey retrieveKeyFromKeyStore(String walletName) throws Exception{
        Log.i(TAG, "Retrieving key for " + walletName);
        KeyStore keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER);
        keyStore.load(null);
        KeyStore.SecretKeyEntry entry = (KeyStore.SecretKeyEntry) keyStore.getEntry(walletName, null);
        Log.i(TAG, "Retrieved key for " + walletName);
        return entry.getSecretKey();
    }

    private static byte[] decryptAES(final byte[] bytes, final byte[] IV, final Key aesKey) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_CIPHER);
        cipher.init(Cipher.DECRYPT_MODE, aesKey, new GCMParameterSpec(GCM_TAG_LENGTH, IV));

        return cipher.doFinal(bytes);
    }
}
