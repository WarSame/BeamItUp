package com.example.graeme.beamitup;

class Eth {
    private String nickname;
    private String address;
    private byte[] encPrivateKey;
    private byte[] iv;
    private long id;
    private long accountId;

    Eth(String address, byte[] encPrivateKey){
        this.address = address;
        this.encPrivateKey = encPrivateKey;
        this.id = -1;
    }

    Eth(String address, byte[] encPrivateKey, byte[] iv, long id, long accountId){
        this.address = address;
        this.encPrivateKey = encPrivateKey;
        this.iv = iv;
        this.id = id;
        this.accountId = accountId;
    }

    Eth(String address, byte[] encPrivateKey, byte[] iv, long accountId){
        this.address = address;
        this.encPrivateKey = encPrivateKey;
        this.iv = iv;
        this.id = -1;
        this.accountId = accountId;
    }

    String getAddress() {
        return address;
    }

    void setAddress(String address) {
        this.address = address;
    }

    byte[] getEncPrivateKey() {
        return encPrivateKey;
    }

    void setEncPrivateKey(byte[] encPrivateKey) {
        this.encPrivateKey = encPrivateKey;
    }

    public long getId() {
        return id;
    }

    void setId(long id) {
        this.id = id;
    }

    byte[] getIv() {
        return iv;
    }

    void setIv(byte[] iv) {
        this.iv = iv;
    }

    long getAccountId() {
        return accountId;
    }

    void setAccountId(long accountId) {
        this.accountId = accountId;
    }

}
