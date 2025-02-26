package com.quwen.common;

import java.util.HashMap;
import java.util.Map;



public class MsgCodeUtil {
    public static final int MSG_CODE_SUCCESS = 0;
    public static final int MSG_CODE_UNKNOWN = -10000;
    public static final int MSG_CODE_FORBIDDEN_LOGIN = -10001;
    public static final int MSG_CODE_JWT_TOKEN_ISNULL = -10002;
    public static final int MSG_CODE_JWT_SIGNATURE_EXCEPTION = -10003;
    public static final int MSG_CODE_JWT_MALFORMED = -10004;
    public static final int MSG_CODE_JWT_TOKEN_EXPIRED = -10005;
    public static final int MSG_CODE_JWT_TOKEN_UNSUPPORTED = -10006;
    public static final int MSG_CODE_JWT_ILLEGAL_ARGUMENT = -10007;
    public static final int MSG_CODE_USERNAME_OR_PASSWORD_INCORRECT = -10008;
    public static final int MSG_CODE_JWT_TOKEN_TYPE_MISMATCH = -10009;
    public static final int MSG_CODE_SYSTEM_ROLE_NAME_EXIST = -10010;
    public static final int MSG_CODE_SYSTEM_ROLE_ENNAME_EXIST = -10011;
    public static final int MSG_CODE_SYSTEM_NOT_SUPER_ADMIN_PERMISSION = -10012;
    public static final int MSG_CODE_SYSTEM_DATABASE_KEY_DUPLICATE = -10013;
    public static final int MSG_CODE_JWT_REFRESH_TOKEN_EXPIRED = -10014;
    public static final int MSG_CODE_REFRESH_TOKEN_FREQUENT = -10015;
    public static final int MSG_CODE_ILLEGAL_ARGUMENT = -10016;
    public static final int MSG_CODE_PARAMETER_IS_NULL = -10017;
    public static final int MSG_CODE_ID_IS_NULL = -10018;
    public static final int MSG_CODE_DATA_NOT_EXIST = -10024;
    public static final int MSG_CODE_DATA_EXIST = -10025;
    public static final int MSG_CODE_PARENT_NODE_NOT_HIMSELF = -10026;
    public static final int MSG_CODE_FLOWINFO_STATE = -10027;
    public static final int MSG_CODE_FLOWINFO_NOT_FOUNT = -10028;
    public static final int MSC_DATA_DROPDATA_ERROR = -10029;
    public static final int MSC_DATA_ADDDATA_ERROR = -10030;
    public static final int MSC_DATA_UPDATADATA_ERROR = -10031;
    public static final int MSG_JWT_NOT_EXIST = -10032;

    public static final int MSG_INVITE_CODE_NOT_EXIST = -10033;
    public static final int MSG_DEVICE_CODE_NOT_AVAILABLE = -10034;
    public static final int MSG_IMAGE_SEND_ERROR = -10035;
    public static final int MSG_DEVICE_CODE_BIND_ERROR = -10036;
    public static final int MSG_CODE_BALANCE_NOT_ENOUGH = -10037;
    public static final int MSG_BALANCE_DEDUCT_ERROR = -10038;

    //自定义微信接口错误码
    public static final int MSG_WECHAT_RESPONSE_EXCEPTION = -20001;
    public static final int MSG_WECHAT_UNKNOWN_EXCEPTION = -20002;
    public static final int MSG_WECHAT_QRCODE_EXCEPTION = -20003;

    //自定义讯飞接口错误码
    public static final int MSG_XUNFEI_AUDIO_TO_TEXT = -30001;
    public static final int MSG_XUNFEI_TEXT_TO_AUDIO = -30002;
    public static final int MSG_XUNFEI_SENSITIVE_ERROR = -30003;

    //自定义OPENAI错误码
    public static final int MSG_OPENAI_GPT_ERROR = -40001;

    // 微信接口错误码
    public static final int MSC_CODE_INVALID = 40029;
    public static final int MSC_API_LIMITED = 45011;
    public static final int MSC_CODE_BLOCKED = 40226;
    public static final int MSC_SYSTEM_ERROR = -1;

    protected static Map<Integer, String> map = new HashMap<>();

    public MsgCodeUtil() {
    }

    public static String getErrMsg(int msgCode) {
        return (String) map.get(msgCode);
    }

    static {
        map.put(0, "请求成功.");
        map.put(-10000, "未知错误.");
        map.put(-10001, "该帐号禁止登录.");
        map.put(-10002, "access token为空.");
        map.put(-10003, "token签名异常.");
        map.put(-10004, "token格式错误.");
        map.put(-10005, "token过期.");
        map.put(-10006, "不支持该token.");
        map.put(-10007, "token参数错误异常.");
        map.put(-10008, "账号或者密码错误.");
        map.put(-10009, "token类型错误.");
        map.put(-10010, "角色名称已经存在.");
        map.put(-10011, "角色英文名称已经存在.");
        map.put(-10012, "越权操作，需超级管理员权限.");
        map.put(-10013, "数据库主键重复.");
        map.put(-10014, "refresh token过期.");
        map.put(-10015, "refresh token刷新频率太高，请稍后再试.");
        map.put(-10016, "非法参数异常:");
        map.put(-10017, "参数为空.");
        map.put(-10018, "参数ID为空.");
        map.put(-10024, "数据不存在：");
        map.put(-10025, "数据存在：");
        map.put(-10026, "父级节点不能是自己");
        map.put(-10029, "删除失败");
        map.put(-10030, "新增失败");
        map.put(-10031, "修改失败");
        map.put(-10032, "不存在该token");

        map.put(-10033, "邀请码不真实");
        map.put(-10034, "设备码不可用,需要申请新设备码");
        map.put(-10035, "无法返回图片文件流");
        map.put(-10036, "设备码绑定失败");
        map.put(-10037, "用户账户余额不足");
        map.put(-10038, "用户账户扣款失败");

        //自定义微信接口错误信息
        map.put(-20001, "微信请求响应异常");
        map.put(-20002, "微信接口未知异常");
        map.put(-20003, "请求微信二维码失败");

        //自定义讯飞接口错误消息
        map.put(-30001, "语音转文字失败,请重试");
        map.put(-30002, "文字转语音失败,请重试");
        map.put(-30003, "敏感词检测失败,请重试");

        //自定义OPENAI错误码
        map.put(-40001, "GPT请求失败");

        // 微信小程序错误信息
        map.put(40029, "js_code无效");
        map.put(45011, "API 调用太频繁，请稍候再试");
        map.put(40226, "高风险等级用户，小程序登录拦截 。");
        map.put(-1, "系统繁忙，此时请开发者稍候再试");

    }
}
