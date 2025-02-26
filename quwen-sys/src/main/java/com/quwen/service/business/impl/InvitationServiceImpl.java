package com.quwen.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quwen.entity.business.Invitation;
import com.quwen.entity.business.InviteHistory;
import com.quwen.mapper.business.InvitationMapper;
import com.quwen.mapper.business.InviteHistoryMapper;
import com.quwen.service.business.InvitationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.ZonedDateTime;

@Service
@Transactional(rollbackFor = Exception.class)
public class InvitationServiceImpl extends ServiceImpl<InvitationMapper, Invitation> implements InvitationService {

    @Resource
    InvitationMapper invitationMapper;

    @Resource
    InviteHistoryMapper inviteHistoryMapper;

    @Override
    public boolean existUser(String userId) {
        return invitationMapper.exists(new LambdaQueryWrapper<Invitation>().eq(Invitation::getUserId,userId));
    }

    @Override
    public boolean checkInviteCode(String code) {
        return invitationMapper.exists(new LambdaQueryWrapper<Invitation>().eq(Invitation::getInviteCode,code));
    }

    @Override
    public boolean invite(String code, String invitedUser, ZonedDateTime inviteTime) {
        Invitation invitation = invitationMapper.selectOne(new LambdaQueryWrapper<Invitation>().eq(Invitation::getInviteCode,code));
        invitation.invite(1);

        InviteHistory inviteHistory = new InviteHistory();
        inviteHistory.setUserId(invitation.getUserId());
        inviteHistory.setInvitedUserId(invitedUser);
        inviteHistory.setInviteTime(inviteTime);

        return invitationMapper.updateById(invitation)>0&&inviteHistoryMapper.insert(inviteHistory)>0;
    }

    @Override
    public Invitation getByUserId(String userId) {
        return invitationMapper.selectOne(new LambdaQueryWrapper<Invitation>().eq(Invitation::getUserId,userId));
    }
}
