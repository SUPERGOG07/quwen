package com.quwen.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quwen.entity.business.InviteHistory;
import com.quwen.entity.business.User;
import com.quwen.mapper.business.InviteHistoryMapper;
import com.quwen.mapper.business.UserMapper;
import com.quwen.service.business.InviteHistoryService;
import com.quwen.vo.InviteHistoryVO;
import com.quwen.vo.PageVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional(rollbackFor = Exception.class)
public class InviteHistoryServiceImpl extends ServiceImpl<InviteHistoryMapper, InviteHistory> implements InviteHistoryService {

    @Resource
    InviteHistoryMapper inviteHistoryMapper;

    @Resource
    UserMapper userMapper;

    @Override
    public PageVO<InviteHistoryVO> getUserInviteHistoryList(String userId, Integer page, Integer size) {

        Page<InviteHistory> inviteHistoryPage = new Page<>(page,size);
        inviteHistoryMapper.selectPage(inviteHistoryPage,new LambdaQueryWrapper<InviteHistory>().eq(InviteHistory::getUserId,userId));

        PageVO<InviteHistoryVO> resultPage = new PageVO<>();
        int total = (int) inviteHistoryPage.getTotal();
        resultPage.setTotal(total);
        resultPage.setPage(page);
        resultPage.setSize(size);
        int totalPage = (total/size)+1;
        resultPage.setTotalPage(totalPage);
        resultPage.setCheckFirst(page==1);
        resultPage.setCheckLast(page==totalPage);

        int num = 0;
        for(InviteHistory history :inviteHistoryPage.getRecords()){
            num++;

            InviteHistoryVO vo = new InviteHistoryVO();
            vo.setInviteTime(history.getInviteTime());
            vo.setInvitedUserId(history.getInvitedUserId());
            User invitedUser = userMapper.selectById(userId);
            vo.setInvitedUserNickname(invitedUser==null?"unknown": invitedUser.getNickname());

            resultPage.put(vo);
        }
        resultPage.setNum(num);

        return resultPage;
    }
}
