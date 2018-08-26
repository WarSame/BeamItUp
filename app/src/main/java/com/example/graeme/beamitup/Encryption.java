package com.example.graeme.beamitup;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.security.keystore.UserNotAuthenticatedException;
import android.util.Log;

import java.security.Key;
import java.security.KeyStore;
import java.security.SecureRandom;

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

    private static final int USER_VALIDATION_DURATION_SECONDS = 300;

    private static final int RANDOM_STRING_LENGTH = 128;
    private static final char[] RANDOM_STRING_CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();

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

    //Generate a long random string as the actual password for the wallet file
    //Generate a key in the keystore with alias walletName
    //Encrypt long password with key
    //Store encrypted blob in DB
    public class Encryptor{
        private boolean isUserAuthenticationRequired = true;
        private byte[] IV;
        private String walletName;
        private byte[] encryptedLongPassword;

        public Encryptor(){
        }

        public Encryptor encryptWalletPassword(String walletName, String longPassword) throws Exception {
            Log.i(TAG, "Encrypting wallet password for " + walletName);

            SecretKey secretKey = generateKey(walletName);
            Cipher cipher = createEncryptionCipher(secretKey);
            byte[] longPasswordBytes = longPassword.getBytes();
            this.setEncryptedLongPassword(cipher.doFinal(longPasswordBytes));
            this.setIV(cipher.getIV());

            Log.i(TAG, "Encrypted wallet password for " + walletName);
            return this;
        }

        private SecretKey generateKey(String walletName) throws Exception {
            Log.i(TAG, "Generating key for " + walletName);

            KeyGenerator keyGen = KeyGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES,
                    KEYSTORE_PROVIDER
            );

            KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(
                    walletName,
                    KeyProperties.PURPOSE_ENCRYPT
                            | KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setRandomizedEncryptionRequired(true);

            if (this.isUserAuthenticationRequired){
                Log.i(TAG, "User authentication required");
                builder.setUserAuthenticationRequired(true)
                    .setUserAuthenticationValidityDurationSeconds(USER_VALIDATION_DURATION_SECONDS);
            }
            else {
                Log.i(TAG, "User authentication is not required");
            }

            keyGen.init(builder.build());

            Log.i(TAG, "Generated key for " + walletName);

            return keyGen.generateKey();
        }

        private Cipher createEncryptionCipher(final Key aesKey) throws Exception {
            Cipher cipher = Cipher.getInstance(AES_CIPHER);
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            return cipher;
        }

        public byte[] getIV() {
            return IV;
        }

        public byte[] getEncryptedLongPassword() {
            return encryptedLongPassword;
        }

        public Encryptor setUserAuthenticationRequired(boolean isUserAuthenticationRequired){
            this.isUserAuthenticationRequired = isUserAuthenticationRequired;
            return this;
        }

        public Encryptor setEncryptedLongPassword(byte[] encryptedLongPassword){
            this.encryptedLongPassword = encryptedLongPassword;
            return this;
        }

        public Encryptor setIV(byte[] IV){
            this.IV = IV;
            return this;
        }

        public String getWalletName() {
            return walletName;
        }

        public Encryptor setWalletName(String walletName) {
            this.walletName = walletName;
            return this;
        }
    }

    //Retrieve encrypted long password from DB
    //Retrieve key with walletName alias
    //Decrypt long password with key
    public class Decryptor {
        public String decryptWalletPassword(byte[] encryptedLongPassword, byte[] IV, String walletName) throws Exception {
            Log.i(TAG, "Decrypting wallet password for " + walletName);
            SecretKey aesKey = retrieveKeyFromKeyStore(walletName);
            byte[] decryptedLongPassword = decryptAES(encryptedLongPassword, IV, aesKey);
            Log.i(TAG, "Decrypted wallet password for " + walletName);
            return new String(decryptedLongPassword);
        }

        private SecretKey retrieveKeyFromKeyStore(String walletName) throws Exception {
            Log.i(TAG, "Retrieving key for " + walletName);
            KeyStore keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER);
            keyStore.load(null);
            KeyStore.SecretKeyEntry entry = (KeyStore.SecretKeyEntry) keyStore.getEntry(walletName, null);
            Log.i(TAG, "Retrieved key for " + walletName);
            return entry.getSecretKey();
        }

        private byte[] decryptAES(final byte[] bytes, final byte[] IV, final Key aesKey) throws Exception {
            Cipher cipher = Cipher.getInstance(AES_CIPHER);
            cipher.init(Cipher.DECRYPT_MODE, aesKey, new GCMParameterSpec(GCM_TAG_LENGTH, IV));

            return cipher.doFinal(bytes);
        }
    }
}
