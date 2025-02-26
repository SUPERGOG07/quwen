package com.quwen.entity.business;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.quwen.entity.base.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * 个人邀请历史类
 */
public class InviteHistory extends BaseEntity<InviteHistory> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户编号
     */
    @NotBlank
    private String userId;

    /**
     * 被邀请用户编号
     */
    @NotBlank
    private String invitedUserId;

    /**
     * 邀请时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss 'GMT'xxx", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss 'GMT'xxx")
    private ZonedDateTime inviteTime;

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
        sb.append(", invitedUserId=").append(invitedUserId);
        sb.append(", inviteTime=").append(inviteTime);

        sb.append("]");
        return sb.toString();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getInvitedUserId() {
        return invitedUserId;
    }

    public void setInvitedUserId(String invitedUserId) {
        this.invitedUserId = invitedUserId;
    }

    public ZonedDateTime getInviteTime() {
        return inviteTime;
    }

    public void setInviteTime(ZonedDateTime inviteTime) {
        this.inviteTime = inviteTime;
    }
}
