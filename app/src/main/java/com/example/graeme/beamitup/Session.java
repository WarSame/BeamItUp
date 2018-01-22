package com.example.graeme.beamitup;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.http.HttpService;

class Session {
    static private Account account;

    static private boolean isAlive;

    static private Web3j web3j;

    private Session(){
    }

    static void createSession(Account account){
        Session.account = account;
        Session.isAlive = true;
        Session.web3j = Web3jFactory.build(
                new HttpService("https://rinkeby.infura.io/SxLC8uFzMPfzwnlXHqx9")
        );
    }

    static void killSession(){
        Session.account = null;
        Session.isAlive = false;
    }

    static Account getUserDetails(){
        return Session.account;
    }

    static Web3j getWeb3j() {
        return Session.web3j;
    }

    static boolean isAlive(){
        return Session.isAlive;
    }
}
