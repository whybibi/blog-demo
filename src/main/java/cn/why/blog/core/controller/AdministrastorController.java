package cn.why.blog.core.controller;

import cn.why.blog.basic.entity.BasicUser;
import cn.why.blog.basic.entity.Role;
import cn.why.blog.basic.entity.UserRole;
import cn.why.blog.basic.service.BasicUserService;
import cn.why.blog.basic.service.RoleService;
import cn.why.blog.basic.service.UserRoleService;
import cn.why.blog.dto.ImgUrlHead;
import cn.why.blog.dto.LayuiPage;
import cn.why.blog.dto.LayuiRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @version: V1.0
 * @auther: whybibi
 * @className: AdministrastorController
 * @date: 2019-06-12 15:07
 * @description: 管理员管理操作接口
 */
@RequestMapping("admin")
@Controller
public class AdministrastorController {

    @Autowired
    private BasicUserService userService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleService roleService;


    @RequiresRoles("admin")
    @PostMapping("admin_list")
    @ResponseBody
    public LayuiPage<BasicUser> getRolePage(LayuiRequest layuiRequest){
        PageHelper.startPage(layuiRequest.getPage(),layuiRequest.getLimit());
        List<UserRole> userRoles = userRoleService.list();
        List<BasicUser> list = new ArrayList<>();
        for(UserRole userRole : userRoles){
            BasicUser user = userService.getOne(new QueryWrapper<BasicUser>().lambda()
                    .eq(BasicUser::getId,userRole.getUserId()));
            user.setRole(roleService.getOne(new QueryWrapper<Role>().lambda().eq(Role::getId,userRole.getRoleId())));
            list.add(user);
        }
        for(BasicUser user : list){
            if(user.getHeadImg() != null && user.getHeadImg().indexOf(ImgUrlHead.url()) == -1)
                user.setHeadImg(ImgUrlHead.url() + user.getHeadImg());
        }
        PageInfo<BasicUser> pageInfo = new PageInfo<>(list);
        return new LayuiPage<BasicUser>(pageInfo.getTotal(),pageInfo.getList());
    }


}
