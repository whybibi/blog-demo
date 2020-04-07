package cn.why.blog.core.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 后台跳转接口控制器
 */
@Controller
@RequestMapping("admin")
public class AdminController {

    @RequiresPermissions("admin:index")
    @GetMapping(value = {"index.html","index","/"})
    public String index(HttpServletRequest request){
        String agent = request.getHeader("user-agent");
        System.out.println(agent);
        return "admin/index";
    }

    /*主界面*/
    @RequiresPermissions("admin:index")
    @GetMapping(value = {"main.html","main"})
    public String main(HttpServletRequest request){
        return "admin/main";
    }


    @GetMapping(value = {"icon.html","icon"})
    public String icon(){
        return "core/icon";
    }

    /*用户查看*/
    @RequiresPermissions("user:view")
    @GetMapping(value = {"user.html","user"})
    public String userView(){
        return "core/user";
    }

    /*角色列表查看*/
    /*@RequiresPermissions("role:view")*/
    @GetMapping(value = {"role.html","role"})
    public String roleView(){
        return "core/role";
    }

    /*动态列表查看*/
    @RequiresPermissions("dynamics:view")
    @GetMapping(value = {"dynamics.html","dynamics"})
    public String dynamicsView(){
        return "core/dynamics";
    }

    /*跳转动态修改弹窗*/
    @RequiresPermissions("dynamics:modify")
    @GetMapping(value = {"dynamics_edit.html","dynamics_edit"})
    public String dynamicsEdit(){
        return "iFrame/dynamics_edit";
    }


    /*跳转权限修改弹窗*/
    /*@RequiresPermissions("permission:tree")*/
    @GetMapping(value = {"permission_tree.html","permission_tree"})
    public String permissionEdit(){
        return "iFrame/permission_tree";
    }

    /*跳转用户新增弹窗*/
    /*@RequiresPermissions("user:add")*/
    @GetMapping(value = {"user_add.html","user_add"})
    public String userAdd(){
        return "iFrame/user_add";
    }

    /*跳转管理员管理*/
    /*@RequiresRoles()*/
    @GetMapping(value = {"comment.html","comment"})
    public String comment(){
        return "core/comment";
    }


    /*跳转评论管理*/
    @RequiresRoles("admin")
    @GetMapping(value = {"administrastor.html","administrastor"})
    public String admin(){
        return "core/administrastor";
    }


    /*退出登陆*/
    @GetMapping("logout")
    public String logout(){
        Subject subject = SecurityUtils.getSubject();
        if(subject != null){
            subject.logout();
        }
        return "core/login";
    }


}
