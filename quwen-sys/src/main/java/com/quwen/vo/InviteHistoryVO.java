package com.quwen.vo;

import com.quwen.entity.business.InviteHistory;

import java.time.ZonedDateTime;

public class InviteHistoryVO {
    private String invitedUserId;

    private String invitedUserNickname;

    private ZonedDateTime inviteTime;

    public String getInvitedUserId() {
        return invitedUserId;
    }

    public void setInvitedUserId(String invitedUserId) {
        this.invitedUserId = invitedUserId;
    }

    public String getInvitedUserNickname() {
        return invitedUserNickname;
    }

    public void setInvitedUserNickname(String invitedUserNickname) {
        this.invitedUserNickname = invitedUserNickname;
    }

    public ZonedDateTime getInviteTime() {
        return inviteTime;
    }

    public void setInviteTime(ZonedDateTime inviteTime) {
        this.inviteTime = inviteTime;
    }
}
