package com.example.graeme.beamitup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class Account implements Serializable {
    static final int MINIMUM_PASSWORD_LENGTH = 4;
    static final int MAXIMUM_PASSWORD_LENGTH = 16;

    private String email;
    private List<Eth> eths;
    private long id;

    Account(String email, long id){
        this.email = email;
        this.id = id;
        this.eths = new ArrayList<>();
    }

    Account(String email, long id, List<Eth> eths){
        this.email = email;
        this.id = id;
        this.eths = eths;
    }

    String getEmail() {
        return email;
    }

    void setEmail(String email) {
        this.email = email;
    }

    List<Eth> getEths() {
        return this.eths;
    }

    void setEths(List<Eth> eths){
        this.eths = eths;
    }

    void addEth(Eth ethId){
        this.eths.add(ethId);
    }

    void removeEth(Eth eth){
        this.eths.remove(eth);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
