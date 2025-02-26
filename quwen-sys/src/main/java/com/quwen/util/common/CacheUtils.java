package com.quwen.util.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.sun.source.doctree.SeeTree;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CacheUtils {

    //AccessToken缓存,key为accessToken的jwtid,value为用户user的id
    private static final LoadingCache<String, String> ACCESS_TOKEN_CACHE = CacheBuilder.newBuilder()
            //初始化容量
            .initialCapacity(1000)
            //最大容量
            .maximumSize(10000)
            //设置写入或更新token一小时后过期
            .expireAfterWrite(1, TimeUnit.HOURS)
            .build(new CacheLoader<String, String>() {

                //这个方法是默认的数据加载实现,get的时候，如果key没有对应的值，就调用这个方法进行加载
                @Override
                public String load(String key) throws Exception {
                    return "null";
                }
            });

    //添加本地AccessToken缓存
    public static void setAccessTokenKey(String key, String value) {
        ACCESS_TOKEN_CACHE.put(key, value);
    }

    //得到本地AccessToken缓存
    public static String getAccessTokenKey(String key) {
        String value;
        try {
            value = ACCESS_TOKEN_CACHE.get(key);
            if (StringUtils.equals("null",value)) {
                return null;
            }
            return value;
        } catch (ExecutionException e) {
            log.error("getAccessKey()方法错误", e);
        }
        return null;
    }




    //RefreshToken调用刷新次数缓存,key为refreshToken的jwtid,value为被调用刷新次数
    private static final LoadingCache<String,Integer> REFRESH_TOKEN_FREQUENCY_CACHE = CacheBuilder.newBuilder()
            .initialCapacity(1000)
            .maximumSize(10000)
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .build(new CacheLoader<String, Integer>() {

                //这个方法是默认的数据加载实现,get的时候，如果key没有对应的值，就调用这个方法进行加载
                @Override
                public Integer load(String key) throws Exception {
                    return 0;
                }
            });


    //refreshToken调用频率+1
    public static void addRefreshFrequency(String key){
        REFRESH_TOKEN_FREQUENCY_CACHE.put(key, getRefreshFrequency(key)+1);
    }

    //获得refreshToken调用频率
    public static Integer getRefreshFrequency(String key){
        try {
            return REFRESH_TOKEN_FREQUENCY_CACHE.get(key);

        } catch (ExecutionException e) {
            log.error("getAccessKey()方法错误", e);
        }
        return 0;
    }





    //deviceCode缓存,key为deviceCode,value为"unknown"或已绑定儿童child账号
    private static final LoadingCache<String,String> DEVICE_CODE_CACHE = CacheBuilder.newBuilder()
            .initialCapacity(1000)
            .maximumSize(10000)
            //过期时间设置十分钟
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build(new CacheLoader<String, String>() {

                //这个方法是默认的数据加载实现,get的时候，如果key没有对应的值，就调用这个方法进行加载
                @Override
                public String load(String key) throws Exception {
                    return "null";
                }
            });

    //新增未绑定设备码
    public static void addDeviceCode(String deviceCode){
        DEVICE_CODE_CACHE.put(deviceCode,"unknown");
    }

    public static boolean existDeviceCode(String deviceCode){
        String value;
        try {
            value = DEVICE_CODE_CACHE.get(deviceCode);
            return !StringUtils.equals("null",value);
        }catch (ExecutionException e) {
            log.error("existDeviceCode()方法错误", e);
        }
        return true;
    }

    //绑定childId与DeviceCode
    public static boolean bindChildToDeviceCode(String deviceCode,String childId){
        try{
            if(StringUtils.equals(DEVICE_CODE_CACHE.get(deviceCode),"unknown")){
                DEVICE_CODE_CACHE.put(deviceCode,childId);
                return true;
            }
        }catch (ExecutionException e) {
            log.error("bindChildToDeviceCode()方法错误", e);
        }
        return false;
    }

    //查看deviceCode绑定信息
    public static String getChildByDeviceCode(String deviceCode){
        String value;
        try {
            value = DEVICE_CODE_CACHE.get(deviceCode);
            if (StringUtils.equals("null",value)) {
                return null;
            }
            return value;
        } catch (ExecutionException e) {
            log.error("getChildByDeviceCode()方法错误", e);
        }
        return null;
    }





    //语音转文字的文字缓存
    private static final LoadingCache<Integer,String> SOCKET_RESULT_CACHE = CacheBuilder.newBuilder()
            .initialCapacity(10)
            .maximumSize(51)
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build(new CacheLoader<Integer, String>() {

                //这个方法是默认的数据加载实现,get的时候，如果key没有对应的值，就调用这个方法进行加载
                @Override
                public String load(Integer key) throws Exception {
                    return "null";
                }
            });

    public static Integer assignSocketCacheKey(){
        int key = 1;
        while (key<51){
//            该值不存在
            if(null == getSocketCacheValue(key)){
                //标记使用
                putInfoIntoSocketCache(key,"##used");
                break;
            }
            key++;
        }
        return key;
    }

    public static String getSocketCacheValue(Integer key){
        String value;
        try {
            value = SOCKET_RESULT_CACHE.get(key);
            //未被分配或未使用
            if (StringUtils.equals("null",value)||StringUtils.equals("##used",value)) {
                return null;
            }
            return value;
        } catch (ExecutionException e) {
            log.error("getSocketCacheValue()方法错误", e);
        }
        return null;
    }

    public static void putInfoIntoSocketCache(Integer key,String value){
        SOCKET_RESULT_CACHE.put(key,value);
    }

    public static void clearSocketCache(Integer key){
        SOCKET_RESULT_CACHE.invalidate(key);
    }




    //文字转语音的语音路径缓存
    private static final LoadingCache<Integer,String> SOCKET_PATH_CACHE = CacheBuilder.newBuilder()
            .initialCapacity(10)
            .maximumSize(51)
            .expireAfterWrite(3, TimeUnit.MINUTES)
            .build(new CacheLoader<Integer, String>() {

                //这个方法是默认的数据加载实现,get的时候，如果key没有对应的值，就调用这个方法进行加载
                @Override
                public String load(Integer key) throws Exception {
                    return "null";
                }
            });

    public static String getPath(Integer key){
        String value;
        try {
            value = SOCKET_PATH_CACHE.get(key);
            //未被分配或未使用
            if (StringUtils.equals("null",value)||StringUtils.equals("##used",value)) {
                return null;
            }
            return value;
        } catch (ExecutionException e) {
            log.error("getSocketCacheValue()方法错误", e);
        }
        return null;
    }

    public static void putPathIntoCache(Integer key,String value){
        SOCKET_PATH_CACHE.put(key,value);
    }

    public static void clearPathCache(Integer key){
        SOCKET_PATH_CACHE.invalidate(key);
    }

    public static Integer assignPathCacheKey(){
        int key = 1;
        while (key<51){
//            该值不存在
            if(null == getPath(key)){
                //标记使用
                putPathIntoCache(key,"##used");
                break;
            }
            key++;
        }
        return key;
    }

}
