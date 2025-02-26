package com.quwen.interceptor;

import com.alibaba.fastjson2.JSON;
import com.quwen.common.CommonResult;
import com.quwen.common.MsgCodeUtil;
import com.quwen.service.business.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Resource
    TokenService tokenService;

    private static final String TOKEN_HEADER = "accessToken";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = request.getHeader(TOKEN_HEADER);
        CommonResult result = new CommonResult().init();

        //令牌存在性验证
        if (token == null) {
            result.fail(MsgCodeUtil.MSG_CODE_JWT_TOKEN_ISNULL);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().print(JSON.toJSONString(result.end()));
            return false;
        }

        //令牌各项验证
        int verifyCode = tokenService.verifyAccessToken(token);

        if (verifyCode != MsgCodeUtil.MSG_CODE_SUCCESS) {
            result.fail(verifyCode);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().print(JSON.toJSONString(result.end()));
            return false;
        }

        return true;
    }
}
