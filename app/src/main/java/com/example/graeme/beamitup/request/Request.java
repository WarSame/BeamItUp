package com.example.graeme.beamitup.request;

import java.io.Serializable;

public class Request implements Serializable {
    private static final String TAG = "Request";
    private static final long serialVersionUID = 7926247977955952013L;
    private String amount;
    private String toAddress;

    public Request(String toAddress, String amount) {
        this.toAddress = toAddress;
        this.amount = amount;
    }

    public String getToAddress() {
        return toAddress;
    }

    public String getAmount() {
        return amount;
    }

    public String toString(){
        return "Amount: "+ this.amount
                + " toAddress: " + this.toAddress;
    }
}
