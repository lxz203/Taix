package com.example.takeataxiproject.litepal;

import org.litepal.crud.LitePalSupport;

import java.util.List;

public class MessageBean extends LitePalSupport {
    String passengerAccountNumber;
    String driverAccountNumber;

    String driverName;
    String  messageList;

    int messageStatus;//1是正在聊天中,2是已结束订单

    String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPassengerAccountNumber() {
        return passengerAccountNumber;
    }

    public void setPassengerAccountNumber(String passengerAccountNumber) {
        this.passengerAccountNumber = passengerAccountNumber;
    }

    public String getDriverAccountNumber() {
        return driverAccountNumber;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public void setDriverAccountNumber(String driverAccountNumber) {
        this.driverAccountNumber = driverAccountNumber;
    }

    public String getMessageList() {
        return messageList;
    }

    public void setMessageList(String messageList) {
        this.messageList = messageList;
    }

    public int getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(int messageStatus) {
        this.messageStatus = messageStatus;
    }
}
