package com.example.graeme.beamitup;

import java.io.Serializable;

class Transfer implements Serializable {
    private String amount;
    private String reason;
    private String senderAddress;
    private String receiverAddress;
    private static final String TAG = "Transfer";

    Transfer(String amount, String reason, String senderPublicKey){
        this.amount = amount;
        this.reason = reason;
        this.senderAddress = senderPublicKey;
        this.receiverAddress = null;
    }

    String getReason() {
        return reason;
    }

    String getAmount() {
        return amount;
    }

    public String toString (){
        return "Amount: " + amount + "\n"
                + "reason: " + reason + "\n"
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
