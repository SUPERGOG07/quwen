package com.quwen.util.xunfei;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONPath;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public class SensitiveUtils {
    /**
     * 1.拼接鉴权
     */
    public static Map<String, String> getAuth(String appid, String apiKey, String apiSecret) throws Exception {
        // 1.获取时间
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String utc = format.format(new Date()); // 如果用postman验证，需要对utc进行urlEncode，然后发起请求

        // 2.控制台关键信息
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put("accessKeyId", apiKey);
        urlParams.put("accessKeySecret", apiSecret);
        urlParams.put("appId", appid);
        urlParams.put("utc", utc);
        // uuid有防重放的功能，如果调试，请注意更换uuid的值
        urlParams.put("uuid", UUID.randomUUID().toString());

        // 3.获取signature
        String signature = signature(apiSecret, urlParams);
        urlParams.put("signature", signature);
        return urlParams;
    }

    /**
     * 2.获取鉴权
     */
    public static String signature(String secret, Map<String, String> queryParam) throws Exception {
        //排序
        TreeMap<String, String> treeMap = new TreeMap<>(queryParam);
        //剔除不参与签名运算的 signature
        treeMap.remove("signature");
        //生成 baseString
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : treeMap.entrySet()) {
            //System.out.println(entry.getKey());
            String value = entry.getValue();
            //参数值为空的不参与签名，
            if (value != null && !value.isEmpty()) {
                //参数值需要 URLEncode
                String encode = URLEncoder.encode(value, StandardCharsets.UTF_8.name());
                builder.append(entry.getKey()).append("=").append(encode).append("&");
            }
        }
        //删除最后位的&符号
        if (builder.length() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        String baseString = builder.toString();
        Mac mac = Mac.getInstance("HmacSHA1");
        SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8.name());
        mac.init(keySpec);
        //得到签名 byte[]
        byte[] signBytes = mac.doFinal(baseString.getBytes(StandardCharsets.UTF_8));
        //将 byte[]base64 编码
        return Base64.getEncoder().encodeToString(signBytes);
    }

    /**
     * 3.发起post请求
     */
    public static String doPostJson(String url, Map<String, String> urlParams, String json) {
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        CloseableHttpResponse closeableHttpResponse = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            String asciiUrl = URI.create(url).toASCIIString();
            RequestBuilder builder = RequestBuilder.post(asciiUrl);
            builder.setCharset(StandardCharsets.UTF_8);
            if (urlParams != null) {
                for (Map.Entry<String, String> entry : urlParams.entrySet()) {
                    builder.addParameter(entry.getKey(), entry.getValue());
                }
            }
            // 创建请求内容
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            builder.setEntity(entity);
            HttpUriRequest request = builder.build();
            // 执行http请求
            closeableHttpResponse = closeableHttpClient.execute(request);
            resultString = EntityUtils.toString(closeableHttpResponse.getEntity(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (closeableHttpResponse != null) {
                    closeableHttpResponse.close();
                }
                if (closeableHttpClient != null) {
                    closeableHttpClient.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultString;
    }

    public static JSONObject str2Json(String json) {
        return JSON.parseObject(json);
    }

    public static boolean checkSession(JSONObject jsonObject) {
        return jsonObject.containsKey("code") &&
                StringUtils.equals(jsonObject.getString("code"), "000000");
    }

    public static String getErrorMsg(JSONObject jsonObject){
        return jsonObject.containsKey("desc")?jsonObject.getString("desc"):null;
    }

    public static boolean checkText(JSONObject jsonObject){
        if(JSONPath.contains(jsonObject,"$.data.result.suggest")){
            String suggest = (String) JSONPath.eval(jsonObject, "$.data.result.suggest");
            return !StringUtils.equals(suggest, "block");
        }
        return true;
    }
}
