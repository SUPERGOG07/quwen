package com.quwen.vo;

import java.time.ZonedDateTime;

public class IncomeVO {

    private double income;

    private String orderStatus;

    private ZonedDateTime tradeTime;

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public ZonedDateTime getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(ZonedDateTime tradeTime) {
        this.tradeTime = tradeTime;
    }
}
