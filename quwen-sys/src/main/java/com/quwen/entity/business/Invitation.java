package com.quwen.entity.business;

import com.quwen.entity.base.BaseEntity;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

/**
 * 个人邀请激励信息类
 */
public class Invitation extends BaseEntity<Invitation> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户编号
     */
    @NotBlank
    private String userId;

    /**
     * 唯一邀请码
     */
    @NotBlank
    private String inviteCode;

    /**
     * 邀请人数
     */
    @PositiveOrZero
    private int inviteNum;

    /**
     * 总收益
     */
    @PositiveOrZero
    private double inviteTotalProfit;

    /**
     * 未提现收益
     */
    @PositiveOrZero
    private double inviteBalance;

    @Value("${inviteSystem.oneReward}")
    private static double oneReward;

    public void invite(int num) {
        this.inviteNum = this.inviteNum + 1;
        this.inviteTotalProfit = this.inviteTotalProfit + oneReward * num;
        this.inviteBalance = this.inviteNum + oneReward * num;
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
        sb.append(", inviteCode=").append(inviteCode);
        sb.append(", inviteNum=").append(inviteNum);
        sb.append(", inviteTotalProfit=").append(inviteTotalProfit);
        sb.append(", inviteBalance=").append(inviteBalance);

        sb.append("]");
        return sb.toString();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public int getInviteNum() {
        return inviteNum;
    }

    public void setInviteNum(int inviteNum) {
        this.inviteNum = inviteNum;
    }

    public double getInviteTotalProfit() {
        return inviteTotalProfit;
    }

    public void setInviteTotalProfit(double inviteTotalProfit) {
        this.inviteTotalProfit = inviteTotalProfit;
    }

    public double getInviteBalance() {
        return inviteBalance;
    }

    public void setInviteBalance(double inviteBalance) {
        this.inviteBalance = inviteBalance;
    }
}
