package cn.why.blog.basic.controller;

import cn.why.blog.utils.PathUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 用户功能跳转接口控制器
 */
@Controller
public class MainController {

    @GetMapping(value = {"/","index","index.html"})
    public String index(){
        return "index";
    }

    @GetMapping(value = {"404","404.html"})
    public String error404(){
        return "global/error";
    }

    @GetMapping(value = {"error","error.html"})
    public String error(){
        return "global/error";
    }

    @GetMapping("iFrame/comment")
    public String comment(){
        return "iFrame/comment";
    }

    @GetMapping("scroll")
    public String scroll(){
        return "basic/scroll";
    }

    @GetMapping("my")
    public String my(){
        return "basic/my";
    }
    @GetMapping("email")
    public String email(){
        return "basic/email";
    }

}
