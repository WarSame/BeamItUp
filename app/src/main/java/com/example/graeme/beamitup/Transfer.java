package com.example.graeme.beamitup;

/**
 * Created by graeme on 2017-10-05.
 */

public class Transfer {
    private String amount;
    private String reason;

    Transfer(String amount, String reason){
        this.amount = amount;
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String toString (){
        return "Amount: " + amount + " " + "reason: " + reason;
    }
}
