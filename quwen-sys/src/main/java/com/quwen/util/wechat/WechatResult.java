package com.quwen.util.wechat;

import com.alibaba.fastjson2.JSONObject;

import java.util.Map;

public class WechatResult {

    private boolean success;

    private int errcode;

    private String errmsg;

    private JSONObject object;

    public WechatResult() {
        this.object = new JSONObject();
    }

    public static WechatResult success(){
        WechatResult result = new WechatResult();
        result.setSuccess(true);
        result.setErrcode(0);
        return result;
    }

    public static WechatResult fail(int errcode,String errmsg){
        WechatResult result = new WechatResult();
        result.setSuccess(false);
        result.setErrcode(errcode);
        result.setErrmsg(errmsg);
        return result;
    }

    public static WechatResult failUnknown(){
        return WechatResult.fail(-20002,"微信接口未知异常");
    }

    public static WechatResult failResponse(){
        return WechatResult.fail(-20001,"微信接口响应异常,响应体为空");
    }

    public void put(String key,Object value){
        this.object.put(key, value);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public JSONObject getObject() {
        return object;
    }

    public void setObject(JSONObject object) {
        this.object = object;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }
}
