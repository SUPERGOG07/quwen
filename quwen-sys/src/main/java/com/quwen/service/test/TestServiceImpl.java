package com.quwen.service.test;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quwen.entity.test.Test;
import com.quwen.mapper.test.TestMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class TestServiceImpl extends ServiceImpl<TestMapper, Test> implements TestService {
}
