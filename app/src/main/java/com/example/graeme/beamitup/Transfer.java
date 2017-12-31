package com.example.graeme.beamitup;

import android.content.Context;
import android.util.Log;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Async;
import org.web3j.utils.Convert;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

class Transfer implements Serializable {
    private String amount;
    private String reason;
    private String senderAddress;
    private String senderPrivateKey;
    private String receiverAddress;
    private static final String TAG = "Transfer";

    Transfer(String amount, String reason, String senderPublicKey){
        this.amount = amount;
        this.reason = reason;
        this.senderAddress = senderPublicKey;
        this.senderPrivateKey = null;
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

    String getSenderPrivateKey() {
        return senderPrivateKey;
    }

    void setSenderPrivateKey(String senderPrivateKey) {
        this.senderPrivateKey = senderPrivateKey;
    }

    String getReceiverAddress() {
        return receiverAddress;
    }

    void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    private String obtainWalletFile(final String WALLET_DIRECTORY) throws Exception {
        File walletdir = new File(WALLET_DIRECTORY);

        if (!walletdir.exists()){
            walletdir.mkdir();
        }

        String fileName = WalletUtils.generateLightNewWalletFile("pass", walletdir);

        Log.d(TAG, "obtainWalletFile: " + fileName);

        return fileName;
    }

    private Credentials obtainCredentials(final String WALLET_DIRECTORY) throws Exception {
        String walletLocation = WALLET_DIRECTORY + obtainWalletFile(WALLET_DIRECTORY);

        Log.d(TAG, "obtainCredentials: " + walletLocation);

        Credentials cred = WalletUtils.loadCredentials(
                this.getSenderPrivateKey(),
                new File(walletLocation)
        );

        Log.d(TAG, "obtainCredentials: address: " + cred.getAddress() + " key: "
                + cred.getEcKeyPair().getPublicKey());
        return cred;
    }

    Future<TransactionReceipt> send(Context context) throws Exception {
        final String WALLET_DIRECTORY = context.getFilesDir() + "/wallets/";

        Callable<TransactionReceipt> task = new Callable<TransactionReceipt>() {
            @Override
            public TransactionReceipt call() throws Exception {
                Web3j web3 = Web3jFactory.build(
                        new HttpService("https://rinkeby.infura.io/SxLC8uFzMPfzwnlXHqx9")
                );
                Log.d(TAG, web3.web3ClientVersion().send().getWeb3ClientVersion());

                Credentials credentials = obtainCredentials(WALLET_DIRECTORY);
                Log.d(TAG, "Credentials retrieved.");

                Log.d(TAG, "Credentials address: " + credentials.getAddress());
                BigInteger balance = web3.ethGetBalance(credentials.getAddress(), DefaultBlockParameterName.LATEST).
                        sendAsync().get().getBalance();
                Log.d(TAG, "Balance: " + balance);

                TransactionReceipt receipt = org.web3j.tx.Transfer.sendFunds(
                        web3,
                        credentials,
                        getReceiverAddress(),
                        BigDecimal.ONE,
                        Convert.Unit.WEI
                ).send();

                Log.d(TAG, receipt.getFrom());
                Log.d(TAG, receipt.getTo());
                return receipt;
            }
        };
        return Async.run(task);
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }
}
