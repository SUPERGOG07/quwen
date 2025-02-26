package com.quwen.service.business.impl;

import com.auth0.jwt.exceptions.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quwen.common.MsgCodeUtil;
import com.quwen.entity.business.Role;
import com.quwen.entity.business.Token;
import com.quwen.mapper.business.TokenMapper;
import com.quwen.service.business.TokenService;
import com.quwen.util.common.JWTUtils;
import com.quwen.util.common.CacheUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class TokenServiceImpl extends ServiceImpl<TokenMapper, Token> implements TokenService {

    @Resource
    TokenMapper tokenMapper;

    @Override
    public int verifyAccessToken(String token) {

        try{
            Map<String,String> claims = JWTUtils.parserAccessToken(token);
            String userId = claims.get("userId");
            String role = claims.get("role");
            String jwtId = claims.get("jti");

            String tokenCache = CacheUtils.getAccessTokenKey(jwtId);
            if(null!=tokenCache && StringUtils.equals(tokenCache,userId)){
                return 0;
            }

            Token selectToken = tokenMapper.selectOne(new LambdaQueryWrapper<Token>().eq(Token::getUserId,userId).eq(Token::getRole,role));

            if(selectToken!=null && selectToken.getAccessTokenId()!=null && StringUtils.equals(selectToken.getAccessTokenId(),jwtId)){
                return 0;
            }

            return MsgCodeUtil.MSG_JWT_NOT_EXIST;

        }catch (AlgorithmMismatchException e){
            return MsgCodeUtil.MSG_CODE_JWT_TOKEN_TYPE_MISMATCH;
        }catch (SignatureVerificationException e){
            return MsgCodeUtil.MSG_CODE_JWT_SIGNATURE_EXCEPTION;
        }catch (TokenExpiredException e){
            return MsgCodeUtil.MSG_CODE_JWT_TOKEN_EXPIRED;
        }catch (MissingClaimException | IncorrectClaimException e){
            return MsgCodeUtil.MSG_CODE_JWT_ILLEGAL_ARGUMENT;
        }catch (JWTVerificationException e){
            return MsgCodeUtil.MSG_CODE_JWT_TOKEN_UNSUPPORTED;
        }

    }

    @Override
    public int verifyRefreshToken(String token) {

        try{
            Map<String,String> claims = JWTUtils.parserRefreshToken(token);
            String userId = claims.get("userId");
            String role = claims.get("role");
            String jwtId = claims.get("jti");

            int refreshFrequency = CacheUtils.getRefreshFrequency(jwtId);
            if(refreshFrequency>1){
                return MsgCodeUtil.MSG_CODE_REFRESH_TOKEN_FREQUENT;
            }

            Token selectToken = tokenMapper.selectOne(new LambdaQueryWrapper<Token>().eq(Token::getUserId,userId).eq(Token::getRole,role));

            if(selectToken!=null && selectToken.getRefreshTokenId()!=null && StringUtils.equals(selectToken.getRefreshTokenId(),jwtId)){
                return 0;
            }

            return MsgCodeUtil.MSG_JWT_NOT_EXIST;

        }catch (AlgorithmMismatchException e){
            return MsgCodeUtil.MSG_CODE_JWT_TOKEN_TYPE_MISMATCH;
        }catch (SignatureVerificationException e){
            return MsgCodeUtil.MSG_CODE_JWT_REFRESH_TOKEN_EXPIRED;
        }catch (TokenExpiredException e){
            return MsgCodeUtil.MSG_CODE_JWT_TOKEN_EXPIRED;
        }catch (MissingClaimException | IncorrectClaimException e){
            return MsgCodeUtil.MSG_CODE_JWT_ILLEGAL_ARGUMENT;
        }catch (JWTVerificationException e){
            return MsgCodeUtil.MSG_CODE_JWT_TOKEN_UNSUPPORTED;
        }

    }


    @Override
    public String refreshAccessToken(String refreshToken) {

        Map<String,String> claims = JWTUtils.parserRefreshToken(refreshToken);
        String userId = claims.get("userId");
        String role = claims.get("role");
        String refreshTokenId = claims.get("jti");
        String accessTokenId = UUID.randomUUID().toString();

        Token token = tokenMapper.selectOne(new LambdaQueryWrapper<Token>()
                .eq(Token::getUserId,userId).eq(Token::getRole,role).eq(Token::getRefreshTokenId,refreshTokenId));
        if(token!=null){
            token.setAccessTokenId(accessTokenId);
            tokenMapper.updateById(token);
        }else {
            token = new Token();
            token.setUserId(userId);
            token.setRole(role);
            token.setAccessTokenId(accessTokenId);
            token.setRefreshTokenId(refreshTokenId);
            tokenMapper.insert(token);
        }

        CacheUtils.addRefreshFrequency(refreshTokenId);
        CacheUtils.setAccessTokenKey(accessTokenId,userId);
        return JWTUtils.createAccessToken(userId,role,accessTokenId);
    }

    @Override
    public String createRefreshToken(String userId, String role) {
        String jwtId = UUID.randomUUID().toString();

        Token token = tokenMapper.selectOne(new LambdaQueryWrapper<Token>()
                .eq(Token::getUserId,userId).eq(Token::getRole,role));
        if(token!=null){
            token.setRefreshTokenId(jwtId);
            token.setAccessTokenId("unknown");
            tokenMapper.updateById(token);
        }else {
            token = new Token();
            token.setUserId(userId);
            token.setRole(role);
            token.setRefreshTokenId(jwtId);
            token.setAccessTokenId("unknown");
            tokenMapper.insert(token);
        }

        if(StringUtils.equals(role, Role.CHILD_WATCH)){
            //儿童手表设置时长6个月,小程序走系统默认过期时间
            long duration = 6*30*24*60*60*1000L;
            return JWTUtils.createCustomRefreshToken(userId,role,duration,jwtId);
        }
        return JWTUtils.createRefreshToken(userId,role,jwtId);
    }

}
