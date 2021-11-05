package com.akshaychavan.bankingapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Akshay Chavan on 05,November,2021
 * akshaychavan.kkwedu@gmail.com
 * BitTerrain Developers
 * https://www.linkedin.com/in/akshaychavan7/
 */


public class TransferSchema {


    @SerializedName("From_ID")
    @Expose
    private Integer fromID;
    @SerializedName("To_ID")
    @Expose
    private Integer toID;
    @SerializedName("Amount_Transferred")
    @Expose
    private Integer amountTransferred;
    @SerializedName("Timestamp")
    @Expose
    private String timestamp;


    public TransferSchema(Integer fromID, Integer toID, Integer amountTransferred, String timestamp) {
        this.fromID = fromID;
        this.toID = toID;
        this.amountTransferred = amountTransferred;
        this.timestamp = timestamp;
    }

    public Integer getFromID() {
        return fromID;
    }

    public void setFromID(Integer fromID) {
        this.fromID = fromID;
    }

    public Integer getToID() {
        return toID;
    }

    public void setToID(Integer toID) {
        this.toID = toID;
    }

    public Integer getAmountTransferred() {
        return amountTransferred;
    }

    public void setAmountTransferred(Integer amountTransferred) {
        this.amountTransferred = amountTransferred;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
