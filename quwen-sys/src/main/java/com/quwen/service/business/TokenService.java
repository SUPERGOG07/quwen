package com.quwen.service.business;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quwen.entity.business.Token;

import java.util.Map;

public interface TokenService extends IService<Token> {

    int verifyAccessToken(String token);

    int verifyRefreshToken(String token);

    String refreshAccessToken(String refreshToken);

    String createRefreshToken(String userId, String role);
}
