package com.example.graeme.beamitup;

import java.io.Serializable;

class Transfer implements Serializable {
    private String amount;
    private String senderAddress;
    private String receiverAddress;
    private static final String TAG = "Transfer";

    Transfer(String amount, String senderAddress){
        this.amount = amount;
        this.senderAddress = senderAddress;
        this.receiverAddress = null;
    }

    String getAmount() {
        return amount;
    }

    public String toString (){
        return "Amount: " + amount + "\n"
                + "sender public key: " + senderAddress + "\n"
                + "receiver public key: " + receiverAddress;
    }

    String getReceiverAddress() {
        return receiverAddress;
    }

    void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    String getSenderAddress() {
        return senderAddress;
    }

    void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }
}
