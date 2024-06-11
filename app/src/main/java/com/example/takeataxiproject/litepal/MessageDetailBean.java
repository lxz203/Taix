package com.example.takeataxiproject.litepal;

import org.litepal.crud.LitePalSupport;

public class MessageDetailBean {
    int messageStatus;
    int ispassenger;
    String message;

    public MessageDetailBean(int messageStatus, int ispassenger, String message) {
        this.messageStatus = messageStatus;
        this.ispassenger = ispassenger;
        this.message = message;
    }

    public int getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(int messageStatus) {
        this.messageStatus = messageStatus;
    }

    public int getIspassenger() {
        return ispassenger;
    }

    public void setIspassenger(int ispassenger) {
        this.ispassenger = ispassenger;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
