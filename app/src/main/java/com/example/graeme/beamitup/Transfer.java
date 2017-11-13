package com.example.graeme.beamitup;

import android.util.Log;

import com.typesafe.config.ConfigFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;

import org.ethereum.config.SystemProperties;
import org.ethereum.core.*;
import org.ethereum.crypto.ECKey;
import org.ethereum.crypto.HashUtil;
import org.ethereum.db.ByteArrayWrapper;
import org.ethereum.facade.Ethereum;
import org.ethereum.facade.EthereumFactory;
import org.ethereum.listener.EthereumListenerAdapter;
import org.ethereum.util.ByteUtil;
import org.spongycastle.util.encoders.Hex;
import org.springframework.context.annotation.Bean;

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

    private static final BigInteger weiInEther = BigInteger.valueOf(1_000_000_000_000_000_000L);
    void sendTransfer(){
        final byte[] senderPrivateKey = HashUtil.sha3("cow".getBytes());
        final byte[] senderAddress = ECKey.fromPrivate(senderPrivateKey).getAddress();

        class SampleConfig extends RopstenSample.RopstenSampleConfig {
            @Bean
            public RopstenSample sampleBean() {
                return new RopstenSample();
            }
        }
        Ethereum ethereum = EthereumFactory.createEthereum(SampleConfig.class);
        BigInteger ethToSend = BigInteger.valueOf(Long.parseLong(this.amount));
        BigInteger weiToSend = weiInEther.multiply(ethToSend);
        BigInteger nonce = ethereum.getRepository().getNonce(senderAddress);

        String receiverAddress = "0x6861B070f43842FC16eAD07854eE5D91B9D27C13";
        Log.d("Transfer", "receiveraddress = " + receiverAddress);

        Transaction tx = new Transaction(
                ByteUtil.bigIntegerToBytes(nonce),
                ByteUtil.longToBytesNoLeadZeroes(ethereum.getGasPrice()),
                ByteUtil.longToBytesNoLeadZeroes(200000),
                Hex.decode(receiverAddress),
                ByteUtil.bigIntegerToBytes(weiToSend),
                new byte[0],
                ethereum.getChainIdForNextBlock()
        );

        tx.sign(ECKey.fromPrivate(senderPrivateKey));
        ethereum.submitTransaction(tx);
    }
}
