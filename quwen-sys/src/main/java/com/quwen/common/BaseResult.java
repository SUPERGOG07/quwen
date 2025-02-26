package com.quwen.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.FieldError;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class BaseResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private int msgCode = 0;

    protected String errMsg;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss 'GMT'xxx", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss 'GMT'xxx")
    private ZonedDateTime receiptDateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss 'GMT'xxx", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss 'GMT'xxx")
    private ZonedDateTime returnDateTime;

    public boolean isSuccess() {
        return this.msgCode >= 0;
    }


    public BaseResult init() {
        this.receiptDateTime = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));
        return this;
    }

    public BaseResult success() {
        int msgCode = 0;
        this.msgCode = msgCode;
        this.errMsg = MsgCodeUtil.getErrMsg(msgCode);
        return this;
    }

    public BaseResult fail(int msgCode) {
        this.msgCode = msgCode;
        this.errMsg = MsgCodeUtil.getErrMsg(msgCode);
        log.error("接口报错. 错误码：{} 错误信息：{}", this.msgCode, this.errMsg);
        return this;
    }

    /**
     * 错误自定义
     * @param msgCode
     * @param errMsg
     * @return
     */
    public BaseResult failCustom(int msgCode, String errMsg) {
        this.msgCode = msgCode;
        if (StringUtils.isBlank(errMsg)) {
            this.errMsg = MsgCodeUtil.getErrMsg(msgCode);
        } else {
            String msgCodeMsg = MsgCodeUtil.getErrMsg(msgCode);
            if (StringUtils.isNotBlank(msgCodeMsg)) {
                this.errMsg = MsgCodeUtil.getErrMsg(msgCode) + errMsg;
            } else {
                this.errMsg = errMsg;
            }
        }

        log.error("接口报错. 错误码：{} 错误信息：{}", this.msgCode, this.errMsg);
        return this;
    }

    public BaseResult failCustom(String errMsg) {
        this.msgCode = -10000;
        this.errMsg = errMsg;
        log.error("接口报错. 错误码：{} 错误信息：{}", this.msgCode, this.errMsg);
        return this;
    }

    public BaseResult failIllegalArgument(List<FieldError> fieldErrors) {
        this.fail(-10016);
        StringBuilder errorStringBuilder = (new StringBuilder(this.errMsg)).append("\n");
        Iterator var3 = fieldErrors.iterator();

        while (var3.hasNext()) {
            FieldError fieldError = (FieldError) var3.next();
            log.error(fieldError.toString());
            errorStringBuilder.append(fieldError.getDefaultMessage()).append("\n");
        }

        this.errMsg = errorStringBuilder.toString();
        return this;
    }

    public BaseResult end() {
        this.returnDateTime = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));
        return this;
    }

    public BaseResult() {
    }

    public BaseResult(int msgCode, String errMsg, ZonedDateTime receiptDateTime, ZonedDateTime returnDateTime) {
        this.msgCode = msgCode;
        this.errMsg = errMsg;
        this.receiptDateTime = receiptDateTime;
        this.returnDateTime = returnDateTime;
    }

    public void error(int msgCode) {
        this.msgCode = msgCode;
        this.errMsg = MsgCodeUtil.getErrMsg(msgCode);
    }

    public static long getSerialVersionUID() {
        return 1L;
    }

    public int getMsgCode() {
        return this.msgCode;
    }

    public void setMsgCode(int msgCode) {
        this.msgCode = msgCode;
    }

    public String getErrMsg() {
        return this.errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public ZonedDateTime getReceiptDateTime() {
        return this.receiptDateTime;
    }

    public void setReceiptDateTime(ZonedDateTime receiptDateTime) {
        this.receiptDateTime = receiptDateTime;
    }

    public ZonedDateTime getReturnDateTime() {
        return this.returnDateTime;
    }

    public void setReturnDateTime(ZonedDateTime returnDateTime) {
        this.returnDateTime = returnDateTime;
    }

    @Override
    public String toString() {
        return "BaseResult{" +
                "msgCode=" + msgCode +
                ", errMsg='" + errMsg + '\'' +
                ", receiptDateTime=" + receiptDateTime +
                ", returnDateTime=" + returnDateTime +
                '}';
    }
}
