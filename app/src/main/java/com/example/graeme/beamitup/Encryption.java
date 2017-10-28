package com.example.graeme.beamitup;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

class Encryption {
    private static final String ENCRYPTION_ALGORITHM = "SHA-256";
    static byte[] hashPassword(String password, byte[] salt) throws NoSuchAlgorithmException{
        MessageDigest md;
        byte[] passwordHash;

        md = MessageDigest.getInstance(ENCRYPTION_ALGORITHM);
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
}
