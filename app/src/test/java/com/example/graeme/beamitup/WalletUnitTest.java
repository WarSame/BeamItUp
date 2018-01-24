package com.example.graeme.beamitup;

import com.example.graeme.beamitup.request.FulfillRequestTask;
import com.example.graeme.beamitup.request.Request;
import com.example.graeme.beamitup.transfer.SendTransactionTask;

import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.io.File;
import java.math.BigDecimal;

public class WalletUnitTest {

    @Test
    public void generateWallet() throws Exception{
        final String PASSWORD = "somepass";
        final String TO_ADDRESS = "0x31B98D14007bDEe637298086988A0bBd31184523";

        File walletDir = new File("E:\\graeme\\Documents\\WalletFiles\\UTC--2018-01-23T23-12-20.095Z--53669ebc1e129296a0fe853ac23108a9c719d700.json");

        Credentials credentials = WalletUtils.loadCredentials(PASSWORD, walletDir);

        Session.createSession();

        System.out.println("Credentials address: " + credentials.getAddress());
        System.out.println("Credentials public key: " + credentials.getEcKeyPair().getPublicKey());
        System.out.println("Credentials private key: " + credentials.getEcKeyPair().getPrivateKey());

        TransactionReceipt transactionReceipt = Transfer.sendFunds(
                Session.getWeb3j(),
                credentials,
                TO_ADDRESS,
                new BigDecimal("0.001"),
                Convert.Unit.ETHER
        ).send();

        System.out.println(transactionReceipt.getFrom());
    }
}
