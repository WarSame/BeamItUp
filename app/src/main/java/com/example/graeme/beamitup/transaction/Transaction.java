package com.example.graeme.beamitup.transaction;

import java.io.Serializable;

public class Transaction implements Serializable {
    public static final String TAG = "Transaction";
    private static final long serialVersionUID = 9124947053743920362L;
    private String amount;
    private long fromID;//Requestee
    private String toAddress;//Requester

    protected Transaction(String toAddress, String amount){
        this.amount = amount;
        this.fromID = -1;
        this.toAddress = toAddress;
    }

    public Transaction() {
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public long getFromID() {
        return fromID;
    }

    public void setFromID(long fromID) {
        this.fromID = fromID;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String toString(){
        return "Amount: "+ this.amount
                + " fromAddress: " + this.fromID
                + " toAddress: " + this.toAddress;
    }
}
