package com.quwen.util.gpt;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Component
@Slf4j
public class GPTUtils {

    @Resource
    RestTemplate restTemplate;

    public String chatGPT(String text) {
        String body = getCustomApi(text);
        if (StringUtils.isBlank(body)) {
            return null;
        }
        return getResultText(body);
    }

    //TODO 做公私钥处理，加上参数信息进行签名加密
    private String getCustomApi(String text) {
        String result;
        try {
            String url = "http://:20235/api/gpt/chat";
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("text", text);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
            result = responseEntity.getBody();
        } catch (Exception e) {
            log.error(e.getMessage());
            result = null;
        }
        return result;
    }

    private String getResultText(String body) {
        JSONObject jsonObject = JSONObject.parseObject(body);
        JSONArray jsonArray = jsonObject.getJSONArray("choices");
        if (jsonArray != null && !jsonArray.isEmpty()) {
            JSONObject choice = jsonArray.getJSONObject(0);
            JSONObject message = choice.getJSONObject("message");
            if (null != message && message.containsKey("content")) {
                return message.getString("content");
            }
        }
        return null;
    }
}

