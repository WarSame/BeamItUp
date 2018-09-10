package com.example.graeme.beamitup.transaction;

import org.web3j.crypto.Credentials;

import java.io.Serializable;

public class Transaction implements Serializable {
    public static final String TAG = "Transaction";
    private static final long serialVersionUID = 9124947053743920362L;
    private String amount;
    private String toAddress;
    private Credentials fromCredentials;

    public Transaction(String toAddress, String amount){
        this.amount = amount;
        this.toAddress = toAddress;
    }

    public Transaction(String toAddress, String amount, Credentials fromCredentials){
        this.toAddress = toAddress;
        this.amount = amount;
        this.fromCredentials = fromCredentials;
    }

    public Transaction() {
    }

    public String getToAddress() {
        return toAddress;
    }

    public String getAmount() {
        return amount;
    }

    Credentials getFromCredentials(){
        return this.fromCredentials;
    }

    void setFromCredentials(Credentials fromCredentials){
        this.fromCredentials = fromCredentials;
    }

    public String toString(){
        return "Amount: "+ this.amount
                + " toAddress: " + this.toAddress;
    }
}
