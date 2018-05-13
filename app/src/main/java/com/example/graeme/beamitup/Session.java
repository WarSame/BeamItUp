package com.example.graeme.beamitup;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.http.HttpService;

public class Session {
    static private boolean isAlive;
    static private Web3j web3j;
    static private final String INFURA_URL = "https://rinkeby.infura.io/SxLC8uFzMPfzwnlXHqx9";

    private Session(){
    }

    public static void createSession(){
        Session.isAlive = true;
        Session.web3j = buildWeb3j();
    }

    private static Web3j buildWeb3j(){
        return Web3jFactory.build(
                new HttpService(INFURA_URL)
        );
    }

    public static void killSession(){
        Session.isAlive = false;
        Session.web3j = null;
    }

    public static Web3j getWeb3j() {
        return Session.web3j;
    }

    public static boolean isAlive(){
        return Session.isAlive;
    }
}
