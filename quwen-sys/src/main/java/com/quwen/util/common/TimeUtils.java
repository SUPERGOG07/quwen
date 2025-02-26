package com.quwen.util.common;

import com.alibaba.fastjson2.JSONObject;

import java.text.ParseException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

public class TimeUtils {

    public static ZonedDateTime str2time(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'GMT'xxx").withZone(ZoneId.of("Asia/Shanghai"));
        try {
            return ZonedDateTime.parse(time, formatter);
        } catch (RuntimeException e){
            return null;
        }
    }

    public static void jsonTimeFormat(JSONObject node) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'GMT'xxx");
        for (Map.Entry<String, Object> entry : node.entrySet()) {
            String key = entry.getKey();
            Object object = entry.getValue();

            if (object instanceof ZonedDateTime) {
                String format = ((ZonedDateTime) object).format(formatter);
                node.put(key, format);
            }
            if (object instanceof JSONObject) {
                jsonTimeFormat((JSONObject) node.get(key));
            }
        }
    }

    public static String getNowTimeFormat() {
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));
        return getTimeFormat(zonedDateTime);
    }

    public static ZonedDateTime getNowTime(){
        return ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));
    }

    public static String getTimeFormat(ZonedDateTime zonedDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'GMT'xxx");
        return zonedDateTime.format(formatter);
    }

    public static boolean validTime(ZonedDateTime zonedDateTime){
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));
        return zonedDateTime.isBefore(now) && zonedDateTime.isAfter(now.plusMinutes(-5L));
    }

}
