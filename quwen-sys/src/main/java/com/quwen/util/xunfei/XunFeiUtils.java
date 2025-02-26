package com.quwen.util.xunfei;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.quwen.util.common.CacheUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.util.Map;

@Component
@Slf4j
@ConfigurationProperties(prefix = "xunfei")
public class XunFeiUtils {

    private String appid;

    private String apiSecret;

    private String apiKey;

    private String VCN;

    @Resource
    RestTemplate restTemplate;


    public String audio2text(FileInputStream is) throws Exception {
        Integer key = CacheUtils.assignSocketCacheKey();
        String url = "https://iat-api.xfyun.cn/v2/iat";
        WebIATWS.startSocket(url, is, key, appid, apiSecret, apiKey);
        int num = 0;
        String result;
        while (true) {
            Thread.sleep(1000L);
            result = CacheUtils.getSocketCacheValue(key);
            if (num > 6 || null != result) {
                CacheUtils.clearSocketCache(key);
                break;
            }
            num++;
        }

        return result;
    }

    public String text2audio(String text) throws Exception {
        Integer key = CacheUtils.assignPathCacheKey();
        log.info("key:{}", key);
        String url = "https://tts-api.xfyun.cn/v2/tts";
        WebTtsWs.startSocket(url, text, key, appid, apiKey, apiSecret, VCN);
        int num = 0;
        String result;
        while (true) {
            Thread.sleep(1000L);
            result = CacheUtils.getPath(key);
            if (num > 15 || null != result) {
                CacheUtils.clearPathCache(key);
                break;
            }
            num++;
        }

        return result;
    }

    /**
     *  0代表通过，-1代表本次连接错误，-2代表文本存在违规内容
     * @param text
     * @return
     * @throws Exception
     */
    public Integer sensitiveCheck(String text) throws Exception {
        String url = "https://audit.iflyaisol.com//audit/v2/syncText";
        Map<String, String> urlParams = SensitiveUtils.getAuth(appid, apiKey, apiSecret);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("content", text);
        jsonObject.put("is_match_all", 1);
        jsonObject.put("categories", new String[]{
                "pornDetection",
                "violentTerrorism",
                "political",
                "contraband",
                "uncivilizedLanguage"});

        String result = SensitiveUtils.doPostJson(url, urlParams, jsonObject.toString());
        JSONObject resultJsonObject = JSON.parseObject(result);
        if(!SensitiveUtils.checkSession(resultJsonObject)){
            return -1;
        }
        if(!SensitiveUtils.checkText(resultJsonObject)){
            return -2;
        }
        return 0;
    }


    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getVCN() {
        return VCN;
    }

    public void setVCN(String VCN) {
        this.VCN = VCN;
    }
}
