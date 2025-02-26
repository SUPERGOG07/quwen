package com.quwen.service.business;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.quwen.entity.business.InviteHistory;
import com.quwen.vo.InviteHistoryVO;
import com.quwen.vo.PageVO;

public interface InviteHistoryService extends IService<InviteHistory> {

    PageVO<InviteHistoryVO> getUserInviteHistoryList(String userId, Integer page, Integer size);
}
