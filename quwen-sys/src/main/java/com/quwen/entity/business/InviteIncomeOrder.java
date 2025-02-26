package com.quwen.entity.business;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.quwen.entity.base.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * 激励金提现订单类
 */
public class InviteIncomeOrder extends BaseEntity<InviteIncomeOrder> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户编号
     */
    @NotBlank
    private String userId;

    /**
     * 提现金额
     */
    @Positive
    private double income;

    /**
     * 商家批次单号
     */
    private String localBatchNumber;

    /**
     * 微信批次单号
     */
    private String wechatBatchNumber;

    /**
     * 微信明细单号
     */
    private String wechatDetailNumber;

    /**
     * 微信订单结果
     */
    private String wechatOrderStatus;

    /**
     * 订单状态
     */
    @NotBlank
    private String orderStatus;

    /**
     * 订单时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss 'GMT'xxx", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss 'GMT'xxx")
    private ZonedDateTime tradeTime;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", createDate=").append(createDate);
        sb.append(", updateDate=").append(updateDate);
        sb.append(", delFlag=").append(delFlag);

        sb.append(", userId=").append(userId);
        sb.append(", income=").append(income);
        sb.append(", localBatchNumber=").append(localBatchNumber);
        sb.append(", wechatBatchNumber=").append(wechatBatchNumber);
        sb.append(", wechatDetailNumber=").append(wechatDetailNumber);
        sb.append(", wechatOrderStatus=").append(wechatOrderStatus);
        sb.append(", orderStatus=").append(orderStatus);
        sb.append(", tradeTime=").append(tradeTime);

        sb.append("]");
        return sb.toString();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public String getLocalBatchNumber() {
        return localBatchNumber;
    }

    public void setLocalBatchNumber(String localBatchNumber) {
        this.localBatchNumber = localBatchNumber;
    }

    public String getWechatBatchNumber() {
        return wechatBatchNumber;
    }

    public void setWechatBatchNumber(String wechatBatchNumber) {
        this.wechatBatchNumber = wechatBatchNumber;
    }

    public String getWechatDetailNumber() {
        return wechatDetailNumber;
    }

    public void setWechatDetailNumber(String wechatDetailNumber) {
        this.wechatDetailNumber = wechatDetailNumber;
    }

    public String getWechatOrderStatus() {
        return wechatOrderStatus;
    }

    public void setWechatOrderStatus(String wechatOrderStatus) {
        this.wechatOrderStatus = wechatOrderStatus;
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
