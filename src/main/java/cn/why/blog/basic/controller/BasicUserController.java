package cn.why.blog.basic.controller;

import cn.why.blog.basic.entity.BasicUser;
import cn.why.blog.basic.service.BasicUserService;
import cn.why.blog.config.shiro.ShiroUtils;
import cn.why.blog.utils.PathUtils;
import com.baomidou.mybatisplus.extension.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("basic_user")
public class BasicUserController {

    @Autowired
    private BasicUserService basicUserService;

    @GetMapping("register_user")
    public String user(){
        return "basic/register_user";
    }

    /**
     * 通过用户id获取某个用户
     * @param id
     * @return
     */
/*    @GetMapping("get_user")
    @ResponseBody
    public Map getUser(Long id){
        Map map = new HashMap();
        map.put("code",200);
        map.put("success",true);
        map.put("basicUser",basicUserService.getBasicUserById(id));
        return map;
    }*/


    /**
     * 用户注册
     * @param
     * @return
     */
    @PostMapping("register")
    @ResponseBody
    public R registerUser(HttpServletRequest request){
        return basicUserService.registerUser(request);
    }

    @PostMapping("current_user")
    @ResponseBody
    public Map getCurrentLoginUser(){
       Map map = new HashMap();
       BasicUser user =  ShiroUtils.getCurrentUser();
       if(user != null){
           user.setSalt("");
           user.setPassWord("");
           if(user.getHeadImg() != null && user.getHeadImg().indexOf("/image/") == -1)
               user.setHeadImg("/image/"+ user.getHeadImg());
          map.put("success",true);
          map.put("user",user);
       }else{
           map.put("success",false);
       }
       return map;
    }



}
