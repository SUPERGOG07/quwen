package com.quwen.util.wechat;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class WechatTokenCacheUtils {

    private static final String TOKEN_KEY = "wechatToken";


    //    声明一个静态的内存块,guava里面的本地缓存
    //构建本地缓存，调用链的方式 ,1000是设置缓存的初始化容量，maximumSize是设置缓存最大容量，当超过了最大容量，guava将使用LRU算法（最少使用算法），来移除缓存项
    //expireAfterAccess(12,TimeUnit.HOURS)设置缓存有效期为1个小时
    private static final LoadingCache<String, String> WECHAT_TOKEN_CACHE = CacheBuilder.newBuilder()
            .initialCapacity(1)
            .maximumSize(5)
            .expireAfterWrite(20, TimeUnit.MINUTES)
            .build(new CacheLoader<String, String>() {

                //这个方法是默认的数据加载实现,get的时候，如果key没有对应的值，就调用这个方法进行加载
                @Override
                public String load(String key) throws Exception {
                    return "null";
                }
            });


    public static void updateToken(String token){
        WECHAT_TOKEN_CACHE.put(TOKEN_KEY, token);
    }


    public static String getToken(){
        String value = null;
        try {
            value = WECHAT_TOKEN_CACHE.get(TOKEN_KEY);
            if ("null".equals(value)) {
                return null;
            }
            return value;
        } catch (ExecutionException e) {
            log.error("getKey()方法错误", e);
        }
        return null;
    }


}
