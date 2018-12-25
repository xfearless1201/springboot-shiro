package org.com.springboot.hardy.core.test;

import org.com.springboot.hardy.core.Bootstrap;
import org.com.springboot.hardy.core.entity.User;
import org.com.springboot.hardy.core.entity.UserInfo;
import org.com.springboot.hardy.core.mapper.UserInfoMapper;
import org.com.springboot.hardy.core.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.hutool.json.JSONUtil;

/**
 * 
 * @ClassName ApplicationTest
 * @Description 测试类
 * @author Hardy
 * @Date 2018年12月10日 下午9:28:57
 * @version 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=Bootstrap.class)
public class ApplicationTest {
    @Autowired
    public UserService userService;
    
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Test
    public void selectUserTest(){
        User user = userService.getByUid((long)1);
        System.err.println(JSONUtil.toJsonStr(user));
        
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(1);
        
        System.err.println(JSONUtil.toJsonStr(userInfo));
    }
}
