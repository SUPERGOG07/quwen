package com.quwen.config;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.support.spring.http.converter.FastJsonHttpMessageConverter;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.quwen.interceptor.RequestLoggerInterceptor;
import com.quwen.interceptor.TokenInterceptor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.JsonbHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
public class QwWebMvcConfig implements WebMvcConfigurer {

    @Resource
    private RequestLoggerInterceptor requestLoggerInterceptor;

    @Resource
    private TokenInterceptor tokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestLoggerInterceptor)
                .addPathPatterns("/**")
                .order(1);
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/error",
                        "/test/**",
                        "/token/refresh",
                        "/user/login",
                        "/child/device",
                        "/child/QRCode/**",
                        "/child/state/**",
                        "/**"
                )
                .order(2);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder){

        return builder
                //设置连接超时时间
                .setConnectTimeout(Duration.ofSeconds(5))
                //设置读取超时时间
                .setReadTimeout(Duration.ofSeconds(20))
                .additionalInterceptors()
                //添加UTF8字符转换器
                .additionalMessageConverters(new StringHttpMessageConverter(StandardCharsets.UTF_8))
                //构建
                .build();
    }

}
