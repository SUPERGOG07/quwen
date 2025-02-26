package com.quwen.controller.business;

import com.quwen.common.CommonResult;
import com.quwen.common.MsgCodeUtil;
import com.quwen.service.business.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/token")
public class TokenController {

    @Resource
    TokenService tokenService;

    @GetMapping("/refresh")
    public CommonResult refreshAccessToken(@RequestParam String refreshToken){

        CommonResult result = new CommonResult().init();
        if(StringUtils.isBlank(refreshToken)){
            result.error(MsgCodeUtil.MSG_CODE_PARAMETER_IS_NULL);
            return (CommonResult) result.end();
        }

        int code = tokenService.verifyRefreshToken(refreshToken);
        if(code!= 0){
            result.fail(code);
            return (CommonResult) result.end();
        }

        String accessToken = tokenService.refreshAccessToken(refreshToken);
        result.success("accessToken",accessToken);
        return (CommonResult) result.end();
    }
}
