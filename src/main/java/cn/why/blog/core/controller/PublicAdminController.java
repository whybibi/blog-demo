package cn.why.blog.core.controller;

import cn.why.blog.basic.entity.BasicUser;
import cn.why.blog.basic.entity.Role;
import cn.why.blog.basic.entity.UserRole;
import cn.why.blog.basic.entity.base.GlobalMessage;
import cn.why.blog.basic.entity.enums.IsEnableEnum;
import cn.why.blog.basic.entity.enums.IsSystemEnum;
import cn.why.blog.basic.entity.enums.SimpleError;
import cn.why.blog.basic.service.BasicUserService;
import cn.why.blog.basic.service.RoleService;
import cn.why.blog.basic.service.UserRoleService;
import cn.why.blog.config.shiro.ShiroUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.Assert;
import com.baomidou.mybatisplus.extension.api.R;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @version: V1.0
 * @auther: whybibi
 * @className: PublicAdminController
 * @date: 2019-06-06 14:27
 * @description: 未登录时不会被shiro拦截的一些公共接口
 */
@Controller
@RequestMapping("public_admin")
public class PublicAdminController {

    @Autowired
    private BasicUserService basicUserService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleService roleService;


    /*跳转后台管理登录*/
    @GetMapping(value = {"dologin.html","dologin"})
    public String login(){
        return "core/login";
    }

    /**
     * 后台用户登录访问
     * @param userName 用户名
     * @param passWord 密码
     * @param roleId 角色ID
     * @return R
     */
    @PostMapping("admin_login")
    @ResponseBody
    @Transactional
    public R adminLogin(String userName, String passWord, Integer roleId){
        if(userName == null || "".equals(userName) || passWord == null || "".equals(passWord) || roleId == null)
            return R.failed(SimpleError.ERROR);
        UsernamePasswordToken token = new UsernamePasswordToken(userName,passWord);
        Subject subject = SecurityUtils.getSubject();
        try{
            subject.login(token);
            UserRole userRole = userRoleService.getOne(new QueryWrapper<UserRole>().lambda()
                    .eq(UserRole::getUserId, ShiroUtils.getCurrentUser().getId())
                    .eq(UserRole::getRoleId,roleId));
            if(userRole == null){
                subject.logout();
                return R.failed(SimpleError.ROLE_NO_FIT);
            }
            BasicUser basicUser = new BasicUser();
            basicUser.setId(ShiroUtils.getCurrentUser().getId());
            basicUser.setLastLoginTime(new Date());
            basicUser.setUpdateDate(new Date());
            basicUser.setUpdateUser(basicUser.getId());
            if(!basicUserService.updateById(basicUser))
                Assert.fail(SimpleError.SAVE_ERROR);
        }catch (UnknownAccountException e){
            return R.failed(SimpleError.NULL_USER);
        }catch(IncorrectCredentialsException e){
            return R.failed(SimpleError.USERNAME_PASSWORD_NOT_EQUALS);
        }catch(LockedAccountException e){
            return R.failed(SimpleError.USER_IS_ENABLE);
        }catch(AuthenticationException e){
            return R.failed(SimpleError.USERNAME_PASSWORD_NOT_EQUALS);
        }catch(Exception e){
            return R.failed(SimpleError.ERROR);
        }
        return R.ok(GlobalMessage.LOGIN_SUCCESS);
    }


    /*后台管理登陆时访问此接口获取角色信息,只返回id及名称*/
    @PostMapping("roles")
    @ResponseBody
    public Map getRolesMap(){
        Map<String,Object> map = new HashMap<>();
        List<Role> list = roleService.list(new QueryWrapper<Role>().lambda()
                .eq(Role::getIsEnable, IsEnableEnum.ONE.getValue())
                .eq(Role::getIsSystem, IsSystemEnum.ONE.getValue()));
        if(list != null && list.size() > 0){
            List<Role> roles = new ArrayList<>();
            for(Role r : list){
                Role role = new Role();
                role.setId(r.getId());
                role.setRoleName(r.getRoleName());
                roles.add(role);
            }
            map.put("success",true);
            map.put("roleList",roles);
        }else{
            map.put("success",false);
        }
        return map;
    }



}
