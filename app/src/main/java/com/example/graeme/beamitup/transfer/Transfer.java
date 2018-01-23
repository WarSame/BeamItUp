package com.example.graeme.beamitup.transfer;

import java.io.Serializable;

public class Transfer implements Serializable, Amountable {
    private String amount;
    private String senderAddress;
    private String receiverAddress;
    private static final String TAG = "Transfer";

    public Transfer(String amount, String senderAddress){
        this.amount = amount;
        this.senderAddress = senderAddress;
        this.receiverAddress = null;
    }

    public String getAmount() {
        return amount;
    }

    public String toString (){
        return "Amount: " + amount + "\n"
                + "sender public key: " + senderAddress + "\n"
                + "receiver public key: " + receiverAddress;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }
}
