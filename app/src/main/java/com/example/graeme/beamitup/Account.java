package com.example.graeme.beamitup;

import android.content.Context;

import java.security.NoSuchAlgorithmException;

class Account {
    static final int MINIMUM_PASSWORD_LENGTH = 4;
    static final int MAXIMUM_PASSWORD_LENGTH = 16;

    private String email;
    private byte[] salt;
    private byte[] passwordHash;

    Account(Context context, String email, String password) throws NoSuchAlgorithmException {
        this.email = email;
        obtainSaltAndHash(context, email, password);
    }

    static void startNewLine(StringBuilder errors) {
        //Prepend text we are going to add with a new line if it is not the first text in the string builder
        if (errors.length() != 0) {
            errors.append("\n");
        }
    }

    private void obtainSaltAndHash(Context context, String email, String password) throws NoSuchAlgorithmException {
        BeamItUpDbHelper db = new BeamItUpDbHelper(context);
        if (db.isEmailInUse(email)){
            this.salt = db.retrieveSalt(email);
            this.passwordHash = Encryption.hashPassword(password, salt);
        }
        else {
            this.salt = Encryption.generateSalt();
            this.passwordHash = Encryption.hashPassword(password, this.salt);
        }
        db.close();
    }

    String getEmail() {
        return email;
    }

    byte[] getPasswordHash() {
        return passwordHash;
    }

    byte[] getSalt() {
        return salt;
    }
}
