package com.example.graeme.beamitup.encryption;

import android.security.keystore.KeyProperties;

import java.security.SecureRandom;

public class Encryption {
    public static final String KEYSTORE_PROVIDER = "AndroidKeyStore";

    public static final String AES_CIPHER = KeyProperties.KEY_ALGORITHM_AES + "/" +
            KeyProperties.BLOCK_MODE_GCM + "/" +
            KeyProperties.ENCRYPTION_PADDING_NONE;
    public static final int GCM_TAG_LENGTH = 128;

    public static final int USER_VALIDATION_DURATION_SECONDS = 300;

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

}
