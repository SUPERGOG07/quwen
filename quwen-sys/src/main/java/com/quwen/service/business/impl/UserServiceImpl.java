package com.quwen.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quwen.entity.business.User;
import com.quwen.mapper.business.UserMapper;
import com.quwen.service.business.UserService;
import com.quwen.util.wechat.WechatHttpUtils;
import com.quwen.util.wechat.WechatResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    UserMapper userMapper;

    @Override
    public User getByOpenid(String openId) {
        return userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getOpenid,openId));
    }

    @Override
    public boolean updateUserNickname(String userId, String nickname) {
        User user = new User();
        user.setId(userId);
        user.setNickname(nickname);
        return userMapper.updateById(user)>0;
    }

    @Override
    public boolean userExist(String userId) {
        return userMapper.exists(new LambdaQueryWrapper<User>().eq(User::getId,userId));
    }

    @Override
    public boolean reduceBalance(String userId) {
        if(null==userId){
            return false;
        }
        User user = userMapper.selectById(userId);
        if(null!=user&&user.getBalance()>0){
            user.setBalance(user.getBalance()-1);
            return userMapper.updateById(user)>0;
        }
        return false;
    }

    @Override
    public boolean checkBalance(String userId) {
        return userMapper.exists(new LambdaQueryWrapper<User>().eq(User::getId,userId).gt(User::getBalance,0));
    }
}
