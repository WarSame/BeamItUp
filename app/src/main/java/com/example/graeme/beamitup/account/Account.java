package com.example.graeme.beamitup.account;

import com.example.graeme.beamitup.eth.Eth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Account implements Serializable {
    static final int MINIMUM_PASSWORD_LENGTH = 4;
    static final int MAXIMUM_PASSWORD_LENGTH = 16;

    private String email;
    private List<Eth> eths;
    private long id;

    public Account(String email, long id){
        this.email = email;
        this.id = id;
        this.eths = new ArrayList<>();
    }

    public Account(String email, long id, List<Eth> eths){
        this.email = email;
        this.id = id;
        this.eths = eths;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Eth> getEths() {
        return this.eths;
    }

    public void setEths(List<Eth> eths){
        this.eths = eths;
    }

    public void addEth(Eth ethId){
        this.eths.add(ethId);
    }

    public void removeEth(Eth eth){
        this.eths.remove(eth);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
