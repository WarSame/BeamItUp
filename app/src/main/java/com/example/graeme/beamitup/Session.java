package com.example.graeme.beamitup;

import com.example.graeme.beamitup.account.Account;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.http.HttpService;

public class Session {
    static private Account account;

    static private boolean isAlive;

    static private Web3j web3j;

    private Session(){
    }

    static void createSession(){//For testing
        Session.web3j = buildWeb3j();
    }

    public static void createSession(Account account){
        Session.account = account;
        Session.isAlive = true;
        Session.web3j = buildWeb3j();
    }

    private static Web3j buildWeb3j(){
        String INFURA_URL = "https://rinkeby.infura.io/SxLC8uFzMPfzwnlXHqx9";
        return Web3jFactory.build(
                new HttpService(INFURA_URL)
        );
    }

    public static void killSession(){
        Session.account = null;
        Session.isAlive = false;
        Session.web3j = null;
    }

    public static Account getUserDetails(){
        return Session.account;
    }

    public static Web3j getWeb3j() {
        return Session.web3j;
    }

    public static boolean isAlive(){
        return Session.isAlive;
    }
}
