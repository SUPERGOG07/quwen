package com.quwen.service.business;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quwen.entity.business.PackageOrder;
import com.quwen.vo.PackageOrderVO;
import com.quwen.vo.PageVO;

public interface PackageOrderService extends IService<PackageOrder> {

    PackageOrder getOneOrder(String userId,String tradeId);

    PageVO<PackageOrderVO> getOrderList(String userId,int page,int size);
}
