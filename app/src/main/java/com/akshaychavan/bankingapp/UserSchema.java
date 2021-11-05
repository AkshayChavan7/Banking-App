package com.akshaychavan.bankingapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Akshay Chavan on 05,November,2021
 * akshaychavan.kkwedu@gmail.com
 * BitTerrain Developers
 * https://www.linkedin.com/in/akshaychavan7/
 */

public class UserSchema {
    @SerializedName("ID")
    @Expose
    private Integer id;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("Balance")
    @Expose
    private Integer balance;


    public UserSchema(Integer id, String name, String email, Integer balance) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.balance = balance;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

}