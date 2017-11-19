package com.example.graeme.beamitup;

import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import org.spongycastle.util.encoders.Hex;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.FastRawTransactionManager;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Async;
import org.web3j.utils.Convert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

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

    private String obtainWalletFile(final String WALLET_DIRECTORY) throws Exception {
        File walletdir = new File(WALLET_DIRECTORY);
        if (!walletdir.exists()){
            walletdir.mkdir();
        }
        String fileName = WalletUtils.generateLightNewWalletFile("pass", walletdir);
        Log.d("TAG", "obtainWalletFile: " + fileName);
        return fileName;
    }

    private Credentials obtainCredentials(final String WALLET_DIRECTORY) throws Exception {
        String walletLocation = WALLET_DIRECTORY + obtainWalletFile(WALLET_DIRECTORY);
        Log.d("TAG", "obtainCredentials: " + walletLocation);
        return WalletUtils.loadCredentials("pass", walletLocation);
    }

    TransactionReceipt sendTransfer(Context context) throws Exception {
        final String WALLET_DIRECTORY = context.getFilesDir() + "/wallets/";

        //Credentials credentials = obtainCredentials(WALLET_DIRECTORY);
        Web3j web3 = Web3jFactory.build(new HttpService("https://rinkeby.infura.io/SxLC8uFzMPfzwnlXHqx9"));

        ClientTransactionManager clientTransactionManager =
                new ClientTransactionManager(web3, "0x6861B070f43842FC16eAD07854eE5D91B9D27C13");

        org.web3j.tx.Transfer tran = new org.web3j.tx.Transfer(web3, clientTransactionManager);

        RemoteCall<TransactionReceipt> rc = tran.sendFunds(
                "0x31B98D14007bDEe637298086988A0bBd31184523",
                BigDecimal.valueOf(1.0),
                Convert.Unit.ETHER);

        return rc.send();
    }
}
