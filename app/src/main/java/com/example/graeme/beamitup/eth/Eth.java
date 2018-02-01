package com.example.graeme.beamitup.eth;

import java.io.Serializable;

public class Eth implements Serializable{
    private String nickname;
    private String address;
    private long id;
    private long accountId;

    public Eth(String nickname, String address, long id, long accountId){
        this.nickname = nickname;
        this.address = address;
        this.id = id;
        this.accountId = accountId;
    }

    public Eth(String nickname, String address, long accountId){
        this.nickname = nickname;
        this.address = address;
        this.id = -1;
        this.accountId = accountId;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
