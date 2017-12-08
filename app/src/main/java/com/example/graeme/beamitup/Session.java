package com.example.graeme.beamitup;

class Session {
    static private Account account;

    static private boolean isAlive;

    private Session(){
    }

    static void createSession(Account account){
        Session.account = account;
        Session.isAlive = true;
    }

    static void killSession(){
        Session.account = null;
        Session.isAlive = false;
    }

    static Account getUserDetails(){
        return Session.account;
    }

    static boolean isAlive(){
        return Session.isAlive;
    }
}
