package com.quwen.vo;

import java.time.ZonedDateTime;

public class PackageOrderVO {

    private String tradeId;

    private String wxTradeId;

    private String packageInfo;

    private double amount;

    private int times;

    private String orderStatus;

    private ZonedDateTime orderInitialTime;

    private ZonedDateTime orderExpireTime;

    private ZonedDateTime orderCompleteTime;

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public String getWxTradeId() {
        return wxTradeId;
    }

    public void setWxTradeId(String wxTradeId) {
        this.wxTradeId = wxTradeId;
    }

    public String getPackageInfo() {
        return packageInfo;
    }

    public void setPackageInfo(String packageInfo) {
        this.packageInfo = packageInfo;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public ZonedDateTime getOrderInitialTime() {
        return orderInitialTime;
    }

    public void setOrderInitialTime(ZonedDateTime orderInitialTime) {
        this.orderInitialTime = orderInitialTime;
    }

    public ZonedDateTime getOrderExpireTime() {
        return orderExpireTime;
    }

    public void setOrderExpireTime(ZonedDateTime orderExpireTime) {
        this.orderExpireTime = orderExpireTime;
    }

    public ZonedDateTime getOrderCompleteTime() {
        return orderCompleteTime;
    }

    public void setOrderCompleteTime(ZonedDateTime orderCompleteTime) {
        this.orderCompleteTime = orderCompleteTime;
    }

}
