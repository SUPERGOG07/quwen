package com.quwen.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.Map;

@Slf4j
@Component
public class RequestLoggerInterceptor implements HandlerInterceptor {

    private static final ThreadLocal<Long> START_TIME_THREAD_LOCAL = new NamedThreadLocal<>("ThreadLocal StartTime");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("***************************************************************************************");
        if (log.isDebugEnabled()) {
            long beginTime = System.currentTimeMillis();
            START_TIME_THREAD_LOCAL.set(beginTime);
            log.debug("开始计时: {}  URI: {}", new SimpleDateFormat("hh:mm:ss.SSS").format(beginTime), request.getRequestURI());
        }

        Map<String, String[]> paramMap = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
            String paramName = entry.getKey();
            String[] paramValues = entry.getValue();
            log.info("请求参数：{}={}", paramName, Arrays.toString(paramValues));
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (log.isDebugEnabled()) {
            long beginTime = START_TIME_THREAD_LOCAL.get();
            long endTime = System.currentTimeMillis();
            Duration duration = Duration.ofMillis(endTime-beginTime);
            log.debug("计时结束:{}  耗时:{}  URI:{}", new SimpleDateFormat("hh:mm:ss.SSS").format(endTime),
                    String.format("%d天%d小时%d分钟%d秒", duration.toDays(), duration.toHoursPart(), duration.toMinutesPart(), duration.toSecondsPart()),
                    request.getRequestURI());
        }
        log.info("=======================================================================================");
        START_TIME_THREAD_LOCAL.remove();
    }
}
