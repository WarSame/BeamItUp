package com.example.graeme.beamitup.eth;

import java.io.Serializable;

public class Eth implements Serializable{
    private String nickname;
    private String address;
    private long id;
    private long accountId;

    public Eth(String address, long id, long accountId){
        this.address = address;
        this.id = id;
        this.accountId = accountId;
    }

    public Eth(String address, long accountId){
        this.address = address;
        this.id = -1;
        this.accountId = accountId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

}
