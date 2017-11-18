package com.example.graeme.beamitup;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

class Transfer implements Serializable {
    private String amount;
    private String reason;
    private String senderPublicKey;
    private String receiverPublicKey;

    Transfer(String amount, String reason, String senderPublicKey){
        this.amount = amount;
        this.reason = reason;
        this.senderPublicKey = senderPublicKey;
        this.receiverPublicKey = null;
    }

    static byte[] toBytes(Transfer tran){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out;
        byte[] bytes = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(tran);
            out.flush();
            bytes = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return bytes;
    }

    static Transfer fromBytes(byte[] bytes){
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = null;
        Object o = null;
        try {
            in = new ObjectInputStream(bis);
            o = in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return (Transfer)o;
    }

    private String getReason() {
        return reason;
    }

    private String getAmount() {
        return amount;
    }

    public String toString (){
        return "Amount: " + amount + "\n"
                + "reason: " + reason + "\n"
                + "sender public key: " + senderPublicKey + "\n"
                + "receiver public key: " + receiverPublicKey;
    }

    private String getSenderPublicKey() {
        return senderPublicKey;
    }

    private void setSenderPublicKey(String senderPublicKey) {
        this.senderPublicKey = senderPublicKey;
    }

    private String getReceiverPublicKey() {
        return receiverPublicKey;
    }

    void setReceiverPublicKey(String receiverPublicKey) {
        this.receiverPublicKey = receiverPublicKey;
    }

    void sendTransfer(){

    }
}
