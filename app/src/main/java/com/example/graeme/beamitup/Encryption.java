package com.example.graeme.beamitup;

import android.content.Context;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.graeme.beamitup.eth.EthDbAdapter;
import com.example.graeme.beamitup.wallet.EncryptedWallet;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.security.auth.x500.X500Principal;

public class Encryption {
    private static final String TAG = "Encryption";
    private static final String HASHING_ALGORITHM = "SHA-256";
    private static final String KEYSTORE_PROVIDER = "AndroidKeyStore";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final String TEXT_FORMAT = "UTF-8";

    private static final String AES_CIPHER = KeyProperties.KEY_ALGORITHM_AES + "/" +
            KeyProperties.BLOCK_MODE_GCM + "/" +
            KeyProperties.ENCRYPTION_PADDING_NONE;
    private static final int GCM_TAG_LENGTH = 128;
    private static final int IV_BYTE_LENGTH = 12;

    private static final int RANDOM_STRING_LENGTH = 128;
    private static final char[] RANDOM_STRING_CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();

    public static byte[] hashPassword(char[] password, byte[] salt) throws NoSuchAlgorithmException{
        MessageDigest md;
        byte[] passwordHash;

        md = MessageDigest.getInstance(HASHING_ALGORITHM);
        md.reset();
        md.update(salt);
        passwordHash = md.digest(toBytes(password));
        Arrays.fill(password, '\0');//Clear password for security

        if (passwordHash == null){
            return null;
        }

        return passwordHash;
    }

    private static byte[] toBytes(char[] chars) {
        CharBuffer charBuffer = CharBuffer.wrap(chars);
        ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(charBuffer);
        byte[] bytes = Arrays.copyOfRange(byteBuffer.array(),
                byteBuffer.position(), byteBuffer.limit());
        Arrays.fill(charBuffer.array(), '\u0000');
        Arrays.fill(byteBuffer.array(), (byte) 0);
        return bytes;
    }

    public static byte[] generateSalt(){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[20];
        random.nextBytes(salt);
        return salt;
    }

    public static class Encryptor {
        private byte[] encryption;
        private byte[] iv;

        public Encryptor(){
        }

        public void encryptPassword(String walletName, String password) throws Exception {
            Log.i(TAG, "Encrypting password with alias of: " + walletName);
            this.encryptText(walletName, password);
        }

        private void encryptText(final String alias, final String textToEncrypt) throws Exception {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(alias));

            this.iv = cipher.getIV();
            this.encryption = cipher.doFinal(textToEncrypt.getBytes(TEXT_FORMAT));
        }

        @NonNull
        private SecretKey getSecretKey(final String alias) throws NoSuchAlgorithmException,
                NoSuchProviderException, InvalidAlgorithmParameterException {

            final KeyGenerator keyGenerator = KeyGenerator
                    .getInstance(KeyProperties.KEY_ALGORITHM_AES, KEYSTORE_PROVIDER);

            keyGenerator.init(new KeyGenParameterSpec.Builder(alias,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build());

            return keyGenerator.generateKey();
        }

        public byte[] getEncryption(){
            return this.encryption;
        }

        public void setEncryption(byte[] encryption){
            this.encryption = encryption;
        }

        public byte[] getIv(){
            return this.iv;
        }

        public void setIv(byte[] iv){
            this.iv = iv;
        }
    }

    public static class Decryptor {
        private KeyStore ks;

        public Decryptor() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
            ks = KeyStore.getInstance(KEYSTORE_PROVIDER);
            ks.load(null);
        }

        public String decryptPassword(String walletName, byte[] privateKeyEnc, byte[] iv) throws Exception {
            return this.decryptText(walletName, privateKeyEnc, iv);
        }

        private String decryptText(final String alias, final byte[] encryptedData, final byte[] encryptionIV) throws Exception {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec spec = new GCMParameterSpec(128, encryptionIV);
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(alias), spec);
            return new String(cipher.doFinal(encryptedData), TEXT_FORMAT);
        }

        private SecretKey getSecretKey(final String alias) throws UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException {
            return ((KeyStore.SecretKeyEntry) ks.getEntry(alias, null)).getSecretKey();
        }
    }

    //Generate a long random string as the actual password for the wallet file
    //Generate a key in the keystore with alias walletName
    //Encrypt long password with key
    //Store encrypted blob in DB
    public static EncryptedWallet encryptWalletPassword(String walletName, String longPassword) throws Exception {
        SecretKey secretKey = generateKey(walletName);
        Cipher cipher = createEncryptionCipher(secretKey);
        byte[] longPasswordBytes = longPassword.getBytes();
        byte[] encryptedLongPassword = cipher.doFinal(longPasswordBytes);
        return new EncryptedWallet(encryptedLongPassword, cipher.getIV(), walletName);
    }

    public static byte[] generateIV(){
        return generateBytes(IV_BYTE_LENGTH);
    }

    private static byte[] generateBytes(int length){
        byte[] bytes = new byte[length];
        SecureRandom sr = new SecureRandom();
        sr.nextBytes(bytes);
        return bytes;
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
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setRandomizedEncryptionRequired(true)
                .build();
        keyGen.init(spec);

        return keyGen.generateKey();
    }

    private static Cipher createEncryptionCipher(final Key aesKey) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_CIPHER);
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        return cipher;
    }

    //Retrieve encrypted blob from DB
    //Retrieve key with walletName alias
    //Decrypt long password with key
    public static String decryptWalletPassword(byte[] encryptedLongPassword, byte[] IV, String walletName) {
        //SecretKey secretKey = retrieveKey(walletName);
        //return decryptAES(encryptedLongPassword, IV, walletName);
        return "";
    }
}
