package com.example.graeme.beamitup;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

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
        } catch (Exception e) {
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

    String sendTransfer(Context context){
        String fileName = "";
        File walletdir = new File(context.getFilesDir() + "wallets");
        try {
            if (!walletdir.exists()){
                walletdir.mkdir();
            }
            //Toast.makeText(context, walletfile.getPath(), Toast.LENGTH_LONG).show();
            fileName = WalletUtils.generateNewWalletFile("pass", walletdir, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileName;
        /*
        Web3j web3 = Web3j.build(new InfuraHttpService("https://rinkeby.infura.io/SxLC8uFzMPfzwnlXHqx9"));
        Web3ClientVersion web3ClientVersion = null;
        String clientVersion = "";
        try {
            web3ClientVersion = web3.web3ClientVersion().send();
            clientVersion = web3ClientVersion.getWeb3ClientVersion();
            Credentials credentials = WalletUtils.loadCredentials("password", "/path/to/walletfile");
            TransactionReceipt receipt = Transfer.sendFunds(
            web3, credentials, "0x<address>|<ensName>",
            BigDecimal.valueOf(1.0), Convert.Unit.ETHER).send();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return clientVersion;
        */
    }
}
