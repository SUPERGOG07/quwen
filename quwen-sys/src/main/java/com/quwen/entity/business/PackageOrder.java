package com.quwen.entity.business;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.quwen.entity.base.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * 个人充值订单
 */
public class PackageOrder extends BaseEntity<PackageOrder> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户编号
     */
    @NotBlank
    private String userId;

    /**
     * 充值金额
     */
    @PositiveOrZero
    private double amount;

    @NotNull
    private String packageInfo;

    /**
     * 套餐包含次数
     */
    @PositiveOrZero
    private int times;

    /**
     * 微信订单号
     */
    private String wechatOrderId;

    /**
     * 微信支付结果
     */
    private String wechatPaymentStatus;

    /**
     * 订单状态
     */
    @NotBlank
    private String orderStatus;

    /**
     * 订单创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss 'GMT'xxx", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss 'GMT'xxx")
    private ZonedDateTime orderInitialTime;

    /**
     * 订单过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss 'GMT'xxx", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss 'GMT'xxx")
    private ZonedDateTime orderExpireTime;

    /**
     * 订单完成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss 'GMT'xxx", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss 'GMT'xxx")
    private ZonedDateTime orderCompleteTime;

    /**
     * 是否是境内
     * 境内：1 境外：0
     * 默认 境内 1
     */
    private String domestic;

    public PackageOrder() {
        super();
        //默认设置 境内 1
        this.domestic = "1";
    }

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
        sb.append(", amount=").append(amount);
        sb.append(", packageInfo=").append(packageInfo);
        sb.append(", times=").append(times);
        sb.append(", wechatOrderId=").append(wechatOrderId);
        sb.append(", wechatPaymentStatus=").append(wechatPaymentStatus);
        sb.append(", orderStatus=").append(orderStatus);
        sb.append(", orderInitialTime=").append(orderInitialTime);
        sb.append(", orderExpireTime=").append(orderExpireTime);
        sb.append(", orderCompleteTime=").append(orderCompleteTime);
        sb.append(", domestic=").append(domestic);

        sb.append("]");
        return sb.toString();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getWechatOrderId() {
        return wechatOrderId;
    }

    public void setWechatOrderId(String wechatOrderId) {
        this.wechatOrderId = wechatOrderId;
    }

    public String getWechatPaymentStatus() {
        return wechatPaymentStatus;
    }

    public void setWechatPaymentStatus(String wechatPaymentStatus) {
        this.wechatPaymentStatus = wechatPaymentStatus;
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

    public String getDomestic() {
        return domestic;
    }

    public void setDomestic(String domestic) {
        this.domestic = domestic;
    }

    public String getPackageInfo() {
        return packageInfo;
    }

    public void setPackageInfo(String packageInfo) {
        this.packageInfo = packageInfo;
    }
}
