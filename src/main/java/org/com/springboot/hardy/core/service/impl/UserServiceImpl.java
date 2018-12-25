package org.com.springboot.hardy.core.service.impl;

import org.com.springboot.hardy.core.entity.User;
import org.com.springboot.hardy.core.mapper.UserMapper;
import org.com.springboot.hardy.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    
    @Autowired
    private UserMapper userMapper;

    public User getByUid(Long uid){
        return userMapper.selectByPrimaryKey(uid);
    }

    @Override
    public User selectByUsername(String username) {
        return userMapper.selectByUsername(username);
    }
    
}
