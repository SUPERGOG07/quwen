package com.quwen.service.business;

import com.baomidou.mybatisplus.extension.service.IService;
import com.quwen.entity.business.User;

public interface UserService extends IService<User> {

    User getByOpenid(String openId);

    boolean updateUserNickname(String userId,String nickname);

    boolean userExist(String userId);

    boolean reduceBalance(String userId);

    boolean checkBalance(String userId);

}
