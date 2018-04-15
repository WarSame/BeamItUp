package com.example.graeme.beamitup.transaction;

import java.io.Serializable;

public class Transaction implements Serializable {
    public static final String TAG = "Transaction";
    private String amount;
    private String fromAddress;//Requestee
    private String toAddress;//Requester

    protected Transaction(String toAddress, String amount){
        this.amount = amount;
        this.fromAddress = null;
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

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String toString(){
        return "Amount: "+ this.amount
                + " fromAddress: " + this.fromAddress
                + " toAddress: " + this.toAddress;
    }
}
