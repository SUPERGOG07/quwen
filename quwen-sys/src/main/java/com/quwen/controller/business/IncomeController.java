package com.quwen.controller.business;

import com.quwen.common.CommonResult;
import com.quwen.common.MsgCodeUtil;
import com.quwen.service.business.InviteIncomeOrderService;
import com.quwen.vo.IncomeVO;
import com.quwen.vo.PageVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/income")
public class IncomeController {

    @Resource
    InviteIncomeOrderService inviteIncomeOrderService;

    @GetMapping("/list/{userId}/{page}/{size}")
    public CommonResult getUserInviteHistoryList(@PathVariable("userId") String userId, @PathVariable("page") Integer page, @PathVariable("size") Integer size) {
        CommonResult result = new CommonResult().init();
        if (StringUtils.isBlank(userId) || page < 1 || size < 1) {
            result.error(MsgCodeUtil.MSG_CODE_ILLEGAL_ARGUMENT);
            return (CommonResult) result.end();
        }

        PageVO<IncomeVO> resultPage = inviteIncomeOrderService.getUserIncomeOrderList(userId, page, size);
        result.success("pageList", resultPage);
        result.success("userId", userId);
        return (CommonResult) result.end();

    }
}
