package com.quwen.runner;

import com.quwen.util.wechat.WechatHttpUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class CustomCommandLineRunner implements CommandLineRunner {

    @Resource
    WechatHttpUtils wechatHttpUtils;

    @Override
    public void run(String... args) throws Exception {
        wechatHttpUtils.updateWechatAccessToken();
    }
}
