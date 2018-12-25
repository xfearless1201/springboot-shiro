package org.com.springboot.hardy.core.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.com.springboot.hardy.core.entity.SysPermission;
import org.com.springboot.hardy.core.entity.SysRole;
import org.com.springboot.hardy.core.entity.User;
import org.com.springboot.hardy.core.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @ClassName ShiroRealm
 * @Description 身份认证realm; (这个需要自己写，账号密码校验；权限等)
 * @author Hardy
 * @Date 2018年12月19日 下午6:48:03
 * @version 1.0.0
 */
public class ShiroRealm extends AuthorizingRealm {

    // 日志
    private static final Logger logger = LoggerFactory.getLogger(ShiroRealm.class);

    @Autowired
    private UserService userService;

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        logger.info("授权开始=======================START=========================");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        User user = (User) principalCollection.getPrimaryPrincipal();
        for (SysRole role : user.getSysRoles()) {
            authorizationInfo.addRole(role.getRole());
            for (SysPermission p : role.getPermissions()) {
                authorizationInfo.addStringPermission(p.getPermission());
            }
        }
        return authorizationInfo;
    }

    /**
     * 认证信息.(身份验证) : Authentication 是用来验证用户身份
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        logger.info("认证信息(身份验证)=======================START=========================");
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        // 获取请求用户名
        String username = token.getUsername();
        // 通过用户名查询用户信息
        User user = userService.selectByUsername(username);
        if (user == null) {
            //非法用户
            throw new UnknownAccountException();
        }
        
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user, user.getPassword(),
                getName());
        return authenticationInfo;
    }

}
