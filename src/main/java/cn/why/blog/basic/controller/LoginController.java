package cn.why.blog.basic.controller;

import cn.why.blog.basic.entity.BasicUser;
import cn.why.blog.basic.entity.base.GlobalMessage;
import cn.why.blog.basic.entity.enums.SimpleError;
import cn.why.blog.basic.service.BasicUserService;
import cn.why.blog.config.shiro.ShiroUtils;
import com.baomidou.mybatisplus.extension.api.R;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
public class LoginController {

    @Autowired
    private BasicUserService basicUserService;

    /**
     * 跳转用户登录
     * @return
     */
    @GetMapping("login")
    public String login(){
        if(ShiroUtils.isAuthenticated()){
            return "redirect:index";
        }
        return "basic/login";
    }

    /**
     * 移动端用户登录
     * @param user
     * @return
     */
    @PostMapping("login")
    @ResponseBody
    public R doLogin(@Validated BasicUser user){
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUserName(),user.getPassWord());
        Subject subject = SecurityUtils.getSubject();
        try{
            subject.login(token);
            BasicUser basicUser = new BasicUser();
            basicUser.setId(ShiroUtils.getCurrentUser().getId());
            basicUser.setLastLoginTime(new Date());
            basicUser.setUpdateDate(new Date());
            basicUser.setUpdateUser(basicUser.getId());
            basicUserService.updateById(basicUser);
            return R.ok(GlobalMessage.LOGIN_SUCCESS);
        }catch (UnknownAccountException e){
            return R.failed(SimpleError.NULL_USER);
        }catch(IncorrectCredentialsException e){
            return R.failed(SimpleError.USERNAME_PASSWORD_NOT_EQUALS);
        }catch(LockedAccountException e){
            return R.failed(SimpleError.USER_IS_ENABLE);
        }catch(AuthenticationException e){
            return R.failed(SimpleError.USERNAME_PASSWORD_NOT_EQUALS);
        }catch(Exception e){
            System.err.println(e.getMessage());
            return R.failed(SimpleError.ERROR);
        }
    }


    /**
     * 退出登陆
     * @return
     */
    @GetMapping("logout")
    public String logout(){
        Subject subject = SecurityUtils.getSubject();
        if(subject != null){
            subject.logout();
        }
        return "basic/login";
    }



}
