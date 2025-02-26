package com.quwen.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Slf4j
@Component
public class QwMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        this.strictInsertFill(metaObject, "createDate", ZonedDateTime.class, ZonedDateTime.now(ZoneId.of("Asia/Shanghai"))); // 起始版本 3.3.0(推荐使用)
        this.strictInsertFill(metaObject, "updateDate", ZonedDateTime.class, ZonedDateTime.now(ZoneId.of("Asia/Shanghai"))); // 起始版本 3.3.0(推荐使用)
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        this.strictUpdateFill(metaObject, "updateDate", ZonedDateTime.class, ZonedDateTime.now(ZoneId.of("Asia/Shanghai"))); // 起始版本 3.3.0(推荐)
    }
}
