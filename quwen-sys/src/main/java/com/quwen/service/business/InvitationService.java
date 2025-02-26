package com.quwen.service.business;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quwen.entity.business.Invitation;

import java.time.ZonedDateTime;

public interface InvitationService extends IService<Invitation> {

    boolean existUser(String userId);

    boolean checkInviteCode(String code);

    boolean invite(String code, String invitedUser, ZonedDateTime inviteTime);

    Invitation getByUserId(String userId);


}
