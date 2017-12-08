package com.example.graeme.beamitup;

import java.io.Serializable;
import java.util.ArrayList;

class Account implements Serializable {
    static final int MINIMUM_PASSWORD_LENGTH = 4;
    static final int MAXIMUM_PASSWORD_LENGTH = 16;

    private String email;
    private ArrayList<Eth> eths;
    private long id;

    Account(String email, long id){
        this.email = email;
        this.id = id;
        this.eths = new ArrayList<>();
    }

    String getEmail() {
        return email;
    }

    ArrayList<Eth> getEths() {
        return this.eths;
    }

    void setEths(ArrayList<Eth> eths){
        this.eths = eths;
    }

    void addEthereumAccount(Eth ethId){
        this.eths.add(ethId);
    }

    void removeEthereumAccount(Eth eth){
        this.eths.remove(eth);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
