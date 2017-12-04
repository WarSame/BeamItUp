package com.example.graeme.beamitup;

import android.content.Context;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

class Account implements Serializable {
    static final int MINIMUM_PASSWORD_LENGTH = 4;
    static final int MAXIMUM_PASSWORD_LENGTH = 16;

    private String email;
    private byte[] salt;
    private byte[] passwordHash;
    private ArrayList<Eth> eths;
    private long id;

    Account(Context context, String email, String password) throws NoSuchAlgorithmException {
        this.email = email;
        obtainSaltAndHash(context, email, password);
        this.eths = new ArrayList<>();
    }

    Account(String email, byte[] passwordHash, byte[] salt, long id){
        this.email = email;
        this.passwordHash = passwordHash;
        this.salt = salt;
        this.id = id;
    }

    static void startNewLine(StringBuilder errors) {
        //Prepend text we are going to add with a new line if it is not the first text in the string builder
        if (errors.length() != 0) {
            errors.append("\n");
        }
    }

    private void obtainSaltAndHash(Context context, String email, String password) throws NoSuchAlgorithmException {
        AccountDbAdapter db = new AccountDbAdapter(context);
        if (db.isEmailInUse(email)){
            this.salt = db.retrieveSalt(email);
        }
        else {
            this.salt = Encryption.generateSalt();
        }
        this.passwordHash = Encryption.hashPassword(password, this.salt);
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

    ArrayList<Eth> getEths() {
        return this.eths;
    }

    void setEths(ArrayList<Eth> eths){
        this.eths = eths;
    }

    void addEthereumAccount(Context context, Eth ethId){
        this.eths.add(ethId);
    }

    void removeEthereumAccount(Eth eth){
        this.eths.remove(eth);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
