package org.com.springboot.hardy.core.service;

import org.com.springboot.hardy.core.entity.User;

public interface UserService {

    public User getByUid(Long uid);
    
    public User selectByUsername(String username);
}
