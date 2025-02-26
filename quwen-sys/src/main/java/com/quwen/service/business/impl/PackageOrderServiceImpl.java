package com.quwen.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quwen.entity.business.PackageOrder;
import com.quwen.mapper.business.PackageOrderMapper;
import com.quwen.service.business.PackageOrderService;
import com.quwen.vo.PackageOrderVO;
import com.quwen.vo.PageVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional(rollbackFor = Exception.class)
public class PackageOrderServiceImpl extends ServiceImpl<PackageOrderMapper, PackageOrder> implements PackageOrderService {

    @Resource
    PackageOrderMapper packageOrderMapper;

    @Override
    public PackageOrder getOneOrder(String userId, String tradeId) {
        PackageOrder packageOrder = packageOrderMapper.selectById(tradeId);
        if(packageOrder!=null&&StringUtils.equals(packageOrder.getUserId(),userId)){
            return packageOrder;
        }
        return null;
    }

    @Override
    public PageVO<PackageOrderVO> getOrderList(String userId, int page, int size) {
        Page<PackageOrder> packageOrderPage = new Page<>(page,size);
        packageOrderMapper.selectPage(packageOrderPage,new LambdaQueryWrapper<PackageOrder>().eq(PackageOrder::getUserId,userId));

        PageVO<PackageOrderVO> resultPage = new PageVO<>();
        int total = (int) packageOrderPage.getTotal();
        resultPage.setTotal(total);
        resultPage.setPage(page);
        resultPage.setSize(size);
        int totalPage = (total/size)+1;
        resultPage.setTotalPage(totalPage);
        resultPage.setCheckFirst(page==1);
        resultPage.setCheckLast(page==totalPage);

        int num = 0;
        for (PackageOrder order:packageOrderPage.getRecords()){
            num++;

            PackageOrderVO vo = new PackageOrderVO();
            vo.setTradeId(order.getId());
            vo.setWxTradeId(order.getWechatOrderId());
            vo.setPackageInfo(order.getPackageInfo());
            vo.setAmount(order.getAmount());
            vo.setTimes(order.getTimes());
            vo.setOrderStatus(order.getOrderStatus());
            vo.setOrderInitialTime(order.getOrderInitialTime());
            vo.setOrderExpireTime(order.getOrderExpireTime());
            vo.setOrderCompleteTime(order.getOrderCompleteTime());

            resultPage.put(vo);
        }
        resultPage.setNum(num);

        return resultPage;
    }
}
