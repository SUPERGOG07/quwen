package com.quwen.entity.business;

import com.quwen.entity.base.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

/**
 * 用户(家长)类
 */
public class User extends BaseEntity<User> {

    private static final long serialVersionUID = 1L;

    /**
     * 微信用户唯一标识
     */
    @NotBlank
    private String openid;

    /**
     * 会话密钥（临时）
     */
    @NotBlank
    private String sessionKey;

    /**
     * 昵称
     */
    @NotBlank
    private String nickname;

    /**
     * 套餐剩余次数
     */
    @PositiveOrZero
    private int balance;

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

        sb.append(", openid=").append(openid);
        sb.append(", sessionKey=").append(sessionKey);
        sb.append(", nickname=").append(nickname);
        sb.append(", balance=").append(balance);

        sb.append("]");
        return sb.toString();
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
