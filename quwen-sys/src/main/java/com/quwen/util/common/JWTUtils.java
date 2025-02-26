package com.quwen.util.common;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
@ConfigurationProperties(prefix = "jwt")
public class JWTUtils {

    private static final String[] PARAMS = {"iss", "iat", "exp", "jti", "userId", "role"};

    /**
     * token前缀
     */
    private static String prefix;

    /**
     * accessToken密钥
     */
    private static String accessTokenSecret;

    /**
     * refreshToken密钥
     */
    private static String refreshTokenSecret;

    /**
     * accessToken设定标准过期时长
     */
    private static long accessTokenStandardExpirationTime;

    /**
     * refreshToken设定标准过期时长
     */
    private static long refreshTokenStandardExpirationTime;

    /**
     * accessToken波动过期时长
     * 大量同时生成的token将导致周期性的高峰，设置波动时间将过期时间错峰有利于减少并发压力
     */
    private static long accessTokenWaveExpirationTime;

    /**
     * refreshToken波动过期时长
     */
    private static long refreshTokenWaveExpirationTime;

    public void setPrefix(String prefix) {
        JWTUtils.prefix = prefix;
    }

    public void setAccessTokenSecret(String accessTokenSecret) {
        JWTUtils.accessTokenSecret = accessTokenSecret;
    }

    public void setRefreshTokenSecret(String refreshTokenSecret) {
        JWTUtils.refreshTokenSecret = refreshTokenSecret;
    }

    public void setAccessTokenStandardExpirationTime(long accessTokenStandardExpirationTime) {
        JWTUtils.accessTokenStandardExpirationTime = accessTokenStandardExpirationTime;
    }

    public void setRefreshTokenStandardExpirationTime(long refreshTokenStandardExpirationTime) {
        JWTUtils.refreshTokenStandardExpirationTime = refreshTokenStandardExpirationTime;
    }

    public void setAccessTokenWaveExpirationTime(long accessTokenWaveExpirationTime) {
        JWTUtils.accessTokenWaveExpirationTime = accessTokenWaveExpirationTime;
    }

    public void setRefreshTokenWaveExpirationTime(long refreshTokenWaveExpirationTime) {
        JWTUtils.refreshTokenWaveExpirationTime = refreshTokenWaveExpirationTime;
    }

    private static String createToken(Map<String, String> sub, long duration, String jwtId, String secret) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTCreator.Builder builder = JWT.create()
                .withIssuer("com.qw")
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + duration))
                .withJWTId(jwtId);
        sub.forEach(builder::withClaim);

        return prefix + builder.sign(algorithm);
    }

    public static String createAccessToken(String userId, String role, String jwtId) {
        Map<String, String> sub = new HashMap<>(2);
        sub.put("userId", userId);
        sub.put("role", role);

        long waveTime = (long) ((Math.random() * 2 - 1) * accessTokenWaveExpirationTime);
        return createToken(sub, accessTokenStandardExpirationTime + waveTime, jwtId, accessTokenSecret);
    }

    public static String createRefreshToken(String userId, String role, String jwtId) {
        Map<String, String> sub = new HashMap<>(2);
        sub.put("userId", userId);
        sub.put("role", role);

        long waveTime = (long) ((Math.random() * 2 - 1) * refreshTokenWaveExpirationTime);
        return createToken(sub, refreshTokenStandardExpirationTime + waveTime, jwtId, refreshTokenSecret);
    }

    public static String createCustomRefreshToken(String userId, String role, long duration, String jwtId) {
        Map<String, String> sub = new HashMap<>(2);
        sub.put("userId", userId);
        sub.put("role", role);

        return createToken(sub, duration, jwtId, refreshTokenSecret);
    }


    public static Map<String, String> parserToken(String token, String secret) throws JWTVerificationException {
        token = token.substring(prefix.length());

        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("com.qw")
                .build();

        DecodedJWT jwt = verifier.verify(token);
        Map<String, Claim> claims = jwt.getClaims();

        Map<String, String> result = new HashMap<>();
        claims.forEach((k, v) -> {
            result.put(k, v.asString());
        });
        return result;
    }

    public static Map<String, String> parserAccessToken(String token) throws JWTVerificationException {
        return parserToken(token, accessTokenSecret);
    }

    public static Map<String, String> parserRefreshToken(String token) throws JWTVerificationException {
        return parserToken(token, refreshTokenSecret);
    }
}
