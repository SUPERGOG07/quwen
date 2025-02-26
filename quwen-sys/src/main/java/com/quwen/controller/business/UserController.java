package com.quwen.controller.business;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.quwen.common.CommonResult;
import com.quwen.common.MsgCodeUtil;
import com.quwen.entity.business.Invitation;
import com.quwen.entity.business.Role;
import com.quwen.entity.business.User;
import com.quwen.service.business.InvitationService;
import com.quwen.service.business.TokenService;
import com.quwen.service.business.UserService;
import com.quwen.util.wechat.WechatHttpUtils;
import com.quwen.util.wechat.WechatResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserService userService;

    @Resource
    InvitationService invitationService;

    @Resource
    TokenService tokenService;

    @Resource
    WechatHttpUtils wechatHttpUtils;

    @PostMapping("/login")
    public CommonResult login(@RequestParam String code) {

        CommonResult result = new CommonResult().init();
        if (StringUtils.isBlank(code)) {
            result.error(MsgCodeUtil.MSG_CODE_PARAMETER_IS_NULL);
            return (CommonResult) result.end();
        }

        WechatResult wechatResult = wechatHttpUtils.wechatLogin(code);
        if (!wechatResult.isSuccess()) {
            result.setMsgCode(wechatResult.getErrcode());
            result.setErrMsg(wechatResult.getErrmsg());
            return (CommonResult) result.end();
        }

        String sessionKey = wechatResult.getObject().getString("session_key");
        String openid = wechatResult.getObject().getString("openid");

        boolean checkNew = false;
        User user = userService.getByOpenid(openid);

        //登录
        if(null != user){

            user.setSessionKey(sessionKey);
            //更新用户sessionKey
            if(!userService.updateById(user)){
                result.setMsgCode(MsgCodeUtil.MSC_DATA_UPDATADATA_ERROR);
                result.setErrMsg("修改用户session_key失败");
                return (CommonResult) result.end();
            }

            //检查用户邀请系统是否被注册过
            if(!invitationService.existUser(user.getId())){
                //没有注册,则注册个人邀请信息
                Invitation invitation = new Invitation();
                invitation.setUserId(user.getId());
                invitation.setInviteCode(String.valueOf(IdWorker.getId()));
                invitation.setInviteNum(0);
                invitation.setInviteTotalProfit(0.00);
                invitation.setInviteBalance(0.00);
                if(!invitationService.save(invitation)){
                    result.setMsgCode(MsgCodeUtil.MSC_DATA_ADDDATA_ERROR);
                    result.setErrMsg("新增用户邀请信息失败");
                    return (CommonResult) result.end();
                }
            }

        //注册
        }else {
            checkNew = true;
            user = new User();
            user.setOpenid(openid);
            user.setSessionKey(sessionKey);
            user.setBalance(0);
            user.setNickname("qid-"+ UUID.randomUUID().toString().substring(0,8));
            //注册用户信息
            if(!userService.save(user)){
                result.setMsgCode(MsgCodeUtil.MSC_DATA_ADDDATA_ERROR);
                result.setErrMsg("新增用户失败");
                return (CommonResult) result.end();
            }

            //注册个人邀请信息
            Invitation invitation = new Invitation();
            invitation.setUserId(user.getId());
            invitation.setInviteCode(String.valueOf(IdWorker.getId()));
            invitation.setInviteNum(0);
            invitation.setInviteTotalProfit(0.00);
            invitation.setInviteBalance(0.00);
            if(!invitationService.save(invitation)){
                result.setMsgCode(MsgCodeUtil.MSC_DATA_ADDDATA_ERROR);
                result.setErrMsg("新增用户邀请信息失败");
                return (CommonResult) result.end();
            }

        }

        String refreshToken = tokenService.createRefreshToken(user.getId(), Role.LIGHT_APP);
        String accessToken = tokenService.refreshAccessToken(refreshToken);

        result.success();
        result.putItem("userId",user.getId());
        result.putItem("openid",user.getOpenid());
        result.putItem("nickname",user.getNickname());
        result.putItem("balance",user.getBalance());
        result.putItem("accessToken",accessToken);
        result.putItem("refreshToken",refreshToken);
        result.putItem("checkNew",checkNew);

        return (CommonResult) result.end();
    }

    @PostMapping("/nickname")
    public CommonResult updateUserNickname(@RequestParam String userId, @RequestParam String nickname){
        CommonResult result = new CommonResult().init();
        if (StringUtils.isBlank(userId)||StringUtils.isBlank(nickname)) {
            result.error(MsgCodeUtil.MSG_CODE_PARAMETER_IS_NULL);
            return (CommonResult) result.end();
        }

        if(userService.updateUserNickname(userId,nickname)){
            result.success("userId",userId);
            result.success("nickname",nickname);
            return (CommonResult) result.end();
        }

        result.fail(MsgCodeUtil.MSC_DATA_UPDATADATA_ERROR);
        return (CommonResult) result.end();
    }

    @GetMapping("/info/{userId}")
    public CommonResult getUserInfo(@PathVariable("userId") String userId){
        CommonResult result = new CommonResult().init();
        if (StringUtils.isBlank(userId)) {
            result.error(MsgCodeUtil.MSG_CODE_PARAMETER_IS_NULL);
            return (CommonResult) result.end();
        }

        User user = userService.getById(userId);
        if(null!=user){
            result.success("userId",user.getId());
            result.success("nickname",user.getNickname());
            result.success("balance",user.getBalance());
            return (CommonResult) result.end();
        }

        result.fail(MsgCodeUtil.MSG_CODE_DATA_NOT_EXIST);
        return (CommonResult) result.end();
    }
}
