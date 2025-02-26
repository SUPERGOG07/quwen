package com.quwen.service.business;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quwen.entity.business.InviteIncomeOrder;
import com.quwen.vo.IncomeVO;
import com.quwen.vo.PageVO;

public interface InviteIncomeOrderService extends IService<InviteIncomeOrder> {

    PageVO<IncomeVO> getUserIncomeOrderList(String userId, int page, int size);
}
