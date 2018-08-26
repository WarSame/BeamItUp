package encryption;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

//Generate a long random string as the actual password for the wallet file
//Generate a key in the keystore with alias walletName
//Encrypt long password with key
//Store encrypted blob in DB
public class Encryptor{
    private final String TAG = "Encryptor";
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
                Encryption.KEYSTORE_PROVIDER
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
                    .setUserAuthenticationValidityDurationSeconds(Encryption.USER_VALIDATION_DURATION_SECONDS);
        }
        else {
            Log.i(TAG, "User authentication is not required");
        }

        keyGen.init(builder.build());

        Log.i(TAG, "Generated key for " + walletName);

        return keyGen.generateKey();
    }

    private Cipher createEncryptionCipher(final Key aesKey) throws Exception {
        Cipher cipher = Cipher.getInstance(Encryption.AES_CIPHER);
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
