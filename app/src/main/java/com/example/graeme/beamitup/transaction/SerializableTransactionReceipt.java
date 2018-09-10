package com.example.graeme.beamitup.transaction;

import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.Serializable;

public class SerializableTransactionReceipt extends TransactionReceipt implements Serializable {
    private static final long serialVersionUID = 5206814773986663323L;
    SerializableTransactionReceipt(TransactionReceipt receipt){
        super(
                receipt.getTransactionHash()
                , receipt.getTransactionIndexRaw()
                , receipt.getBlockHash()
                , receipt.getBlockNumberRaw()
                , receipt.getCumulativeGasUsedRaw()
                , receipt.getGasUsedRaw()
                , receipt.getContractAddress()
                , receipt.getRoot()
                , receipt.getStatus()
                , receipt.getFrom()
                , receipt.getTo()
                , receipt.getLogs()
                , receipt.getLogsBloom()
        );
    }
}
