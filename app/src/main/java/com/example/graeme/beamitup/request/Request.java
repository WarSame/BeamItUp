package com.example.graeme.beamitup.request;

import java.io.Serializable;
import com.example.graeme.beamitup.transfer.Amountable;

public class Request implements Serializable, Amountable {
    private static final String TAG = "Request";

    private String amount;
    private String fromAddress;//Requestee
    private String toAddress;//Requester

    Request(String toAddress, String amount){
        this.amount = amount;
        this.fromAddress = null;
        this.toAddress = toAddress;
    }

    String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    void setFromAddress(String fromAddress) {
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
