package com.quwen.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quwen.entity.business.InviteIncomeOrder;
import com.quwen.mapper.business.InviteIncomeOrderMapper;
import com.quwen.service.business.InviteIncomeOrderService;
import com.quwen.vo.IncomeVO;
import com.quwen.vo.PageVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional(rollbackFor = Exception.class)
public class InviteIncomeOrderServiceImpl extends ServiceImpl<InviteIncomeOrderMapper, InviteIncomeOrder> implements InviteIncomeOrderService {

    @Resource
    InviteIncomeOrderMapper inviteIncomeOrderMapper;

    @Override
    public PageVO<IncomeVO> getUserIncomeOrderList(String userId, int page, int size) {

        Page<InviteIncomeOrder> inviteIncomeOrderPage = new Page<>(page,size);
        inviteIncomeOrderMapper.selectPage(inviteIncomeOrderPage,new LambdaQueryWrapper<InviteIncomeOrder>().eq(InviteIncomeOrder::getUserId,userId));

        PageVO<IncomeVO> resultPage = new PageVO<>();
        int total = (int) inviteIncomeOrderPage.getTotal();
        resultPage.setTotal(total);
        resultPage.setPage(page);
        resultPage.setSize(size);
        int totalPage = (total/size)+1;
        resultPage.setTotalPage(totalPage);
        resultPage.setCheckFirst(page==1);
        resultPage.setCheckLast(page==totalPage);

        int num = 0;
        for(InviteIncomeOrder order: inviteIncomeOrderPage.getRecords()){
            num++;

            IncomeVO vo = new IncomeVO();
            vo.setIncome(order.getIncome());
            vo.setOrderStatus(order.getOrderStatus());
            vo.setTradeTime(order.getTradeTime());

            resultPage.put(vo);
        }
        resultPage.setNum(num);

        return resultPage;
    }
}
