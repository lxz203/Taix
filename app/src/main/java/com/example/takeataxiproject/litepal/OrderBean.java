package com.example.takeataxiproject.litepal;

import org.litepal.crud.LitePalSupport;

public class OrderBean extends LitePalSupport {
    String passengerAccountNumber;

    boolean isDriverOrder;

    String driverAccountNumber;

    String price;

    String distance;
    

    String startPointName;

    String endPointName;

    int orderStatus;//0 默认状态，1未接单，2已接单，3已取消,4已完成

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getStartPointName() {
        return startPointName;
    }

    public void setStartPointName(String startPointName) {
        this.startPointName = startPointName;
    }

    public String getEndPointName() {
        return endPointName;
    }

    public void setEndPointName(String endPointName) {
        this.endPointName = endPointName;
    }

    public String getPassengerAccountNumber() {
        return passengerAccountNumber;
    }

    public void setPassengerAccountNumber(String passengerAccountNumber) {
        this.passengerAccountNumber = passengerAccountNumber;
    }

    public boolean isDriverOrder() {
        return isDriverOrder;
    }

    public void setDriverOrder(boolean driverOrder) {
        isDriverOrder = driverOrder;
    }

    public String getDriverAccountNumber() {
        return driverAccountNumber;
    }

    public void setDriverAccountNumber(String driverAccountNumber) {
        this.driverAccountNumber = driverAccountNumber;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "OrderBean{" +
                "passengerAccountNumber='" + passengerAccountNumber + '\'' +
                ", isDriverOrder=" + isDriverOrder +
                ", driverAccountNumber='" + driverAccountNumber + '\'' +
                ", price='" + price + '\'' +
                ", distance='" + distance + '\'' +
                ", startPointName='" + startPointName + '\'' +
                ", endPointName='" + endPointName + '\'' +
                ", orderStatus=" + orderStatus +
                '}';
    }
}
