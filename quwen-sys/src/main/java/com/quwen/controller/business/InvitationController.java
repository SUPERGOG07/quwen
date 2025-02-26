package com.quwen.controller.business;

import com.quwen.common.CommonResult;
import com.quwen.common.MsgCodeUtil;
import com.quwen.entity.business.Invitation;
import com.quwen.service.business.InvitationService;
import com.quwen.service.business.InviteHistoryService;
import com.quwen.util.common.TimeUtils;
import com.quwen.vo.InviteHistoryVO;
import com.quwen.vo.PageVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.ZonedDateTime;

@RestController
@RequestMapping("/invitation")
public class InvitationController {

    @Resource
    InvitationService invitationService;

    @Resource
    InviteHistoryService inviteHistoryService;


    @PutMapping("/commit")
    public CommonResult commitInvitationCode(@RequestParam String userId, @RequestParam String inviteCode, @RequestParam String inviteTime) {

        CommonResult result = new CommonResult().init();
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(inviteCode) || StringUtils.isBlank(inviteTime)) {
            result.error(MsgCodeUtil.MSG_CODE_PARAMETER_IS_NULL);
            return (CommonResult) result.end();
        }

        ZonedDateTime inviteZonedDateTime = TimeUtils.str2time(inviteTime);

        if (!invitationService.checkInviteCode(inviteCode)) {
            result.fail(MsgCodeUtil.MSG_INVITE_CODE_NOT_EXIST);
            result.putItem("ok", false);
            return (CommonResult) result.end();
        }

        if (invitationService.invite(inviteCode, userId, inviteZonedDateTime)) {
            result.success("ok", true);
            return (CommonResult) result.end();
        }

        result.error(MsgCodeUtil.MSC_DATA_ADDDATA_ERROR);
        return (CommonResult) result.end();
    }

    @GetMapping("/info/{userId}")
    public CommonResult getUserInvitationInfo(@PathVariable("userId") String userId) {
        CommonResult result = new CommonResult().init();
        if (StringUtils.isBlank(userId)) {
            result.error(MsgCodeUtil.MSG_CODE_PARAMETER_IS_NULL);
            return (CommonResult) result.end();
        }

        Invitation invitation = invitationService.getByUserId(userId);
        if (null == invitation) {
            result.fail(MsgCodeUtil.MSG_CODE_DATA_NOT_EXIST);
            return (CommonResult) result.end();
        }

        result.success("userId", invitation.getUserId());
        result.success("inviteCode", invitation.getInviteCode());
        result.success("inviteNum", invitation.getInviteNum());
        result.success("inviteTotalProfit", invitation.getInviteTotalProfit());
        result.success("inviteBalance", invitation.getInviteBalance());
        return (CommonResult) result.end();
    }

    @GetMapping("/history/{userId}/{page}/{size}")
    public CommonResult getUserInviteHistoryList(@PathVariable("userId") String userId, @PathVariable("page") Integer page, @PathVariable("size") Integer size) {
        CommonResult result = new CommonResult().init();
        if (StringUtils.isBlank(userId) || page < 1 || size < 1) {
            result.error(MsgCodeUtil.MSG_CODE_ILLEGAL_ARGUMENT);
            return (CommonResult) result.end();
        }

        PageVO<InviteHistoryVO> resultPage = inviteHistoryService.getUserInviteHistoryList(userId, page, size);
        result.success("pageList", resultPage);
        result.success("userId", userId);
        return (CommonResult) result.end();

    }
}
