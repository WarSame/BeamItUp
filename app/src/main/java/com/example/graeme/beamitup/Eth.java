package com.example.graeme.beamitup;

import java.io.Serializable;

class Eth implements Serializable{
    private String nickname;
    private String address;
    private long id;
    private long accountId;

    Eth(String address, long id, long accountId){
        this.address = address;
        this.id = id;
        this.accountId = accountId;
    }

    Eth(String address, long accountId){
        this.address = address;
        this.id = -1;
        this.accountId = accountId;
    }

    String getAddress() {
        return address;
    }

    void setAddress(String address) {
        this.address = address;
    }

    public long getId() {
        return id;
    }

    void setId(long id) {
        this.id = id;
    }

    long getAccountId() {
        return accountId;
    }

    void setAccountId(long accountId) {
        this.accountId = accountId;
    }

}
