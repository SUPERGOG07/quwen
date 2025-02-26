package com.quwen.util.wechat;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Component
@Slf4j
@ConfigurationProperties(prefix = "wechat")
public class WechatHttpUtils {

    private String appid;

    private String appSecret;

    private String loginPage;

    private String env;

    private int width;

    @Resource
    RestTemplate restTemplate;

    public void updateWechatAccessToken() {

        if (StringUtils.isBlank(appid) && StringUtils.isBlank(appSecret)) {
            log.error("更新微信access_token失败,原因: {}", "appid和appsecret为空");
            return;
        }

        String url = "https://api.weixin.qq.com/cgi-bin/stable_token";
        JSONObject params = new JSONObject();
        params.put("grant_type", "client_credential");
        params.put("appid", appid);
        params.put("secret", appSecret);

        ResponseEntity<JSONObject> responseEntity = restTemplate.postForEntity(url, params, JSONObject.class);
        JSONObject responseEntityBody = responseEntity.getBody();

        if (responseEntityBody == null) {
            log.error("更新微信access_token失败,原因: {}", "响应体为空");
            return;
        }

        if (responseEntityBody.containsKey("access_token")) {

            String accessToken = responseEntityBody.getString("access_token");
            if (StringUtils.isNotBlank(accessToken)) {
                WechatTokenCacheUtils.updateToken(accessToken);
                log.info("更新微信access_token成功,时间:{} ,token: {}", LocalDateTime.now(), accessToken);
                return;
            }

        } else if (responseEntityBody.containsKey("errmsg")) {

            String errmsg = responseEntityBody.getString("errmsg");
            if (StringUtils.isNotBlank(errmsg)) {
                log.error("更新微信access_token失败,原因: {}", errmsg);
                return;
            }
        }

        log.error("更新微信access_token失败,原因: {}", "Unknown");

    }

    private static String getAccessToken() {
        String accessToken = WechatTokenCacheUtils.getToken();
        if (StringUtils.isBlank(accessToken)) {
            log.error("微信AccessToken为空，不可发起请求");
            return null;
        }
        return accessToken;
    }

    public WechatResult wechatLogin(String code) {

        String url = "https://api.weixin.qq.com/sns/jscode2session?" +
                "appid=" + appid.trim() +
                "&secret=" + appSecret.trim() +
                "&js_code=" + code.trim() +
                "&grant_type=authorization_code";

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        String responseEntityBody = responseEntity.getBody();

        if (StringUtils.isBlank(responseEntityBody)) {
            log.error("微信小程序登录接口请求失败,原因: {}", "响应体为空");
            return WechatResult.failResponse();
        }

        JSONObject jsonObject = JSON.parseObject(responseEntityBody);
        log.info(jsonObject.toString());

        if(jsonObject.containsKey("session_key")&&jsonObject.containsKey("openid")){
            WechatResult result = WechatResult.success();
            result.put("session_key", jsonObject.getString("session_key"));
            result.put("openid", jsonObject.getString("openid"));
            return result;
        }

        if (jsonObject.containsKey("errcode")) {

            int errcode = jsonObject.getIntValue("errcode");
            if (errcode == 0) {
                WechatResult result = WechatResult.success();
                result.put("session_key", jsonObject.getString("session_key"));
                result.put("openid", jsonObject.getString("openid"));
                return result;
            }

            if (jsonObject.containsKey("errmsg") && jsonObject.containsKey("errcode")) {

                String errmsg = jsonObject.getString("errmsg");
                if (StringUtils.isNotBlank(errmsg)) {
                    log.error("微信小程序登录接口请求失败,原因: {}", errmsg);
                    return WechatResult.fail(errcode, String.format("微信小程序登录接口请求失败,原因: %s", errmsg));
                }
            }
        }

        log.error("微信小程序登录接口请求失败,原因: {}", "Unknown");
        return WechatResult.failUnknown();
    }

    public InputStream getWechatQRCode(String deviceCode) {
        String accessToken = getAccessToken();
        if (accessToken == null) {
            log.error("微信小程序获取二维码失败，原因：{}", "accessToken为空");
            return null;
        }
        String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + accessToken.trim();
        JSONObject params = new JSONObject();
        params.put("scene", "deviceCode=" + deviceCode.trim());
        params.put("page", loginPage);
        params.put("check_path", false);
        params.put("env_version", env);
        params.put("width", width);

        ResponseEntity<byte[]> responseEntity = restTemplate.postForEntity(url, params, byte[].class);
        if (responseEntity.getBody() == null) {
            log.error("微信小程序获取二维码失败,原因：{}", "响应体为空");
            return null;
        }
        MediaType contentType = responseEntity.getHeaders().getContentType();
        if (contentType == null || !contentType.equalsTypeAndSubtype(MediaType.IMAGE_JPEG)) {
            log.error("微信小程序获取二维码失败,原因：{}", new String(responseEntity.getBody(), StandardCharsets.UTF_8));
            return null;
        }

        byte[] body = responseEntity.getBody();
        return new ByteArrayInputStream(body);
    }


    public void setAppid(String appid) {
        this.appid = appid;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getAppid() {
        return appid;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public String getLoginPage() {
        return loginPage;
    }

    public void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
