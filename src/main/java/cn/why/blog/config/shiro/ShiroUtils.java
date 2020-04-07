package cn.why.blog.config.shiro;

import cn.why.blog.basic.entity.BasicUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;

/**
 * Shiro工具类
 */
public class ShiroUtils {

    /**
     * 获得当前登陆用户
     * @return
     */
    public static BasicUser getCurrentUser(){
        BasicUser user = (BasicUser) SecurityUtils.getSubject().getPrincipal();
        return user;
    }

    /**
     * 查询当前是否已经登陆
     * @return
     */
    public static boolean isAuthenticated(){
        return SecurityUtils.getSubject().isAuthenticated();
    }

    /**
     * 设置当前登陆用户
     * @param user
     */
    public static void setUser(BasicUser user) {
        Subject subject = SecurityUtils.getSubject();
        PrincipalCollection principalCollection = subject.getPrincipals();
        String realmName = principalCollection.getRealmNames().iterator().next();
        PrincipalCollection newPrincipalCollection =
                new SimplePrincipalCollection(user, realmName);
        subject.runAs(newPrincipalCollection);
    }




}
