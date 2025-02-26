package com.quwen.entity.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * 基本实体类
 */
public abstract class BaseEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final int DEL_FLAG_NOT_DELETE = 0;
    public static final int DEL_FLAG_DELETE = 1;

    @TableId
    protected String id;

    @TableField(fill = FieldFill.INSERT)
    protected ZonedDateTime createDate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected ZonedDateTime updateDate;

    @TableLogic
    protected int delFlag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss 'GMT'xxx",
            timezone = "GMT+8"
    )
    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss 'GMT'xxx",
            timezone = "GMT+8"
    )
    public ZonedDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(ZonedDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public int getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(int delFlag) {
        this.delFlag = delFlag;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
