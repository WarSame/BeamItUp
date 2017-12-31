package com.example.graeme.beamitup;

import android.os.Parcel;
import android.os.Parcelable;
//TODO remove parcelable
class Eth implements Parcelable {
    private String nickname;
    private String address;
    private byte[] encPrivateKey;
    private byte[] iv;
    private long id;
    private long accountId;

    Eth(){
        this.id = -1;
    }

    Eth(String address, byte[] encPrivateKey){
        this.address = address;
        this.encPrivateKey = encPrivateKey;
        this.id = -1;
    }

    protected Eth(Parcel in) {
        address = in.readString();
        encPrivateKey = in.createByteArray();
        iv = in.createByteArray();
        id = in.readLong();
        accountId = in.readLong();
    }

    public static final Creator<Eth> CREATOR = new Creator<Eth>() {
        @Override
        public Eth createFromParcel(Parcel in) {
            return new Eth(in);
        }

        @Override
        public Eth[] newArray(int size) {
            return new Eth[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address);
        dest.writeByteArray(encPrivateKey);
        dest.writeByteArray(iv);
        dest.writeLong(id);
        dest.writeLong(accountId);
    }
}
