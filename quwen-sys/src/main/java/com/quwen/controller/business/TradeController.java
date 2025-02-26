package com.quwen.controller.business;

import com.quwen.common.CommonResult;
import com.quwen.common.MsgCodeUtil;
import com.quwen.entity.business.PackageOrder;
import com.quwen.service.business.PackageOrderService;
import com.quwen.vo.PackageOrderVO;
import com.quwen.vo.PageVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/trade")
public class TradeController {

    @Resource
    PackageOrderService packageOrderService;

    @GetMapping("/query/{userId}/{tradeId}")
    public CommonResult queryOneTrade(@PathVariable("userId") String userId, @PathVariable("tradeId") String tradeId) {
        CommonResult result = new CommonResult().init();
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(tradeId)) {
            result.error(MsgCodeUtil.MSG_CODE_PARAMETER_IS_NULL);
            return (CommonResult) result.end();
        }

        PackageOrder packageOrder = packageOrderService.getOneOrder(userId, tradeId);
        if (packageOrder != null) {
            result.success("userId", userId);
            result.success("tradeId", tradeId);
            result.success("wxTradeId", packageOrder.getWechatOrderId());
            result.success("packageInfo", packageOrder.getPackageInfo());
            result.success("amount", packageOrder.getAmount());
            result.success("times", packageOrder.getTimes());
            result.success("orderStatus", packageOrder.getOrderStatus());
            result.success("orderInitialTime", packageOrder.getOrderInitialTime());
            result.success("orderExpireTime", packageOrder.getOrderExpireTime());
            result.success("orderCompleteTime", packageOrder.getOrderCompleteTime());
            return (CommonResult) result.end();
        }

        result.fail(MsgCodeUtil.MSG_CODE_DATA_NOT_EXIST);
        return (CommonResult) result.end();
    }

    @GetMapping("/list/{userId}/{page}/{size}")
    public CommonResult getUserInviteHistoryList(@PathVariable("userId") String userId, @PathVariable("page") Integer page, @PathVariable("size") Integer size) {
        CommonResult result = new CommonResult().init();
        if (StringUtils.isBlank(userId) || page < 1 || size < 1) {
            result.error(MsgCodeUtil.MSG_CODE_ILLEGAL_ARGUMENT);
            return (CommonResult) result.end();
        }

        PageVO<PackageOrderVO> resultPage = packageOrderService.getOrderList(userId, page, size);
        result.success("pageList", resultPage);
        result.success("userId", userId);
        return (CommonResult) result.end();

    }
}
