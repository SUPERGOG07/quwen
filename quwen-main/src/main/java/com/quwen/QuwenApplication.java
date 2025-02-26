package com.quwen;

import com.quwen.util.common.JWTUtils;
import com.quwen.util.wechat.WechatHttpUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.quwen.mapper")
@ComponentScan(basePackages = {"com.quwen.*"})
@EnableConfigurationProperties({JWTUtils.class, WechatHttpUtils.class})
@EnableScheduling
@EnableOpenApi
public class QuwenApplication {
    public static void main(String[] args) {
        SpringApplication.run(QuwenApplication.class, args);
    }
}
