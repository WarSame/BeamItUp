package com.example.graeme.beamitup;

import java.io.Serializable;

class Eth implements Serializable {
    private String address;
    private byte[] encPrivateKey;
    private long id;

    Eth(String address, byte[] encPrivateKey){
        this.address = address;
        this.encPrivateKey = encPrivateKey;
        this.id = -1;
    }

    String getAddress() {
        return address;
    }

    byte[] getEncPrivateKey() {
        return encPrivateKey;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
