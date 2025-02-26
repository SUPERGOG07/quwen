package com.quwen.mapper.test;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quwen.entity.test.Test;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestMapper extends BaseMapper<Test> {
}
