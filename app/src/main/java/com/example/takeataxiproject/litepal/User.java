package com.example.takeataxiproject.litepal;

import org.litepal.crud.LitePalSupport;

public class User extends LitePalSupport {
    String account;
    String passoword;
    boolean isPassengers;
    String userName;

    int money;

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassoword() {
        return passoword;
    }

    public void setPassoword(String passoword) {
        this.passoword = passoword;
    }

    public boolean isPassengers() {
        return isPassengers;
    }

    public void setPassengers(boolean passengers) {
        isPassengers = passengers;
    }
}
