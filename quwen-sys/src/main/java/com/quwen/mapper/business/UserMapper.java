package com.quwen.mapper.business;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quwen.entity.business.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
