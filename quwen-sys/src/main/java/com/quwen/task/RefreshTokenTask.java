package com.quwen.task;

import com.quwen.util.wechat.WechatHttpUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RefreshTokenTask {

    @Resource
    WechatHttpUtils wechatHttpUtils;

    @Scheduled(cron = "0 0/5 * * * ?")
    public void refreshWechatToken() {
        wechatHttpUtils.updateWechatAccessToken();
    }

}
