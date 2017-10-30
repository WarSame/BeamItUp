package com.example.graeme.beamitup;

class Eth {
    private String address;
    private byte[] encPrivateKey;

    Eth(String address, byte[] encPrivateKey){
        this.address = address;
        this.encPrivateKey = encPrivateKey;
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
}
