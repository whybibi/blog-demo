package cn.why.blog.config.shiro;

import cn.why.blog.basic.entity.BasicUser;
import cn.why.blog.basic.entity.Role;
import cn.why.blog.basic.entity.RolePermission;
import cn.why.blog.basic.entity.enums.IsDeleteEnum;
import cn.why.blog.basic.entity.enums.IsEnableEnum;
import cn.why.blog.basic.service.BasicUserService;
import cn.why.blog.basic.service.RolePermissionService;
import cn.why.blog.basic.service.UserRoleService;
import cn.why.blog.dto.ImgUrlHead;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

public class UserRealm extends AuthorizingRealm{

    @Autowired
    private BasicUserService basicUserService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RolePermissionService rolePermissionService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        BasicUser user = (BasicUser) principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Role role = userRoleService.getRoleByUserId(user.getId());
        if(role != null){
            info.addRole(role.getRoleCode());
            info.setStringPermissions(rolePermissionService.getPermissionStringByRoleId(Integer.parseInt(String.valueOf(role.getId()))));
        }
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String username = (String) token.getPrincipal();
        BasicUser user = basicUserService.getBasicUserByName(username);
        if(user == null)
            throw new UnknownAccountException();
        if(IsDeleteEnum.ZERO.getValue().equals(user.getIsDelete()))
            throw new UnknownAccountException (); // 帐号删除
        if(IsEnableEnum.ZERO.getValue().equals(user.getIsEnable()))
            throw new LockedAccountException(); // 帐号锁定
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(
                user,
                user.getPassWord(),
                ByteSource.Util.bytes(user.getSalt()),
                getName()
        );
        Session session = SecurityUtils.getSubject().getSession();
        if(user.getHeadImg() != null && user.getHeadImg().indexOf(ImgUrlHead.url()) == -1)
            user.setHeadImg(ImgUrlHead.url()+ user.getHeadImg());
        user.setSalt("");
        user.setPassWord("");
        session.setAttribute("userSession",user);
        return info;
    }
}
