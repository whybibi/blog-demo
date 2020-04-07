package cn.why.blog.core.controller;

import cn.why.blog.basic.entity.Permission;
import cn.why.blog.basic.entity.Role;
import cn.why.blog.basic.entity.RolePermission;
import cn.why.blog.basic.entity.UserRole;
import cn.why.blog.basic.entity.base.GlobalMessage;
import cn.why.blog.basic.entity.enums.IsDeleteEnum;
import cn.why.blog.basic.entity.enums.IsEnableEnum;
import cn.why.blog.basic.entity.enums.IsSystemEnum;
import cn.why.blog.basic.entity.enums.SimpleError;
import cn.why.blog.basic.service.PermissionService;
import cn.why.blog.basic.service.RolePermissionService;
import cn.why.blog.basic.service.RoleService;
import cn.why.blog.basic.service.UserRoleService;
import cn.why.blog.config.shiro.ShiroUtils;
import cn.why.blog.core.entity.TreeHolder;
import cn.why.blog.dto.LayuiPage;
import cn.why.blog.dto.LayuiRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.Assert;
import com.baomidou.mybatisplus.extension.api.R;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * 角色后台管理
 */
@RequestMapping("admin")
@Controller
public class RoleController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private RolePermissionService rolePermissionService;
    @Autowired
    private UserRoleService userRoleService;


    /**
     * 新增角色，只有admin才能新增
     * @param role
     * @return
     */
    @RequiresRoles("admin")
    @PostMapping("add_role")
    @ResponseBody
    @Transactional
    public R addRole(@Validated Role role){
        if(roleService.isExistRoleCodeOrName(role.getRoleCode(),role.getRoleName())){
            return R.failed(SimpleError.EXIST_NAME_ERROR);
        }
        role.setCreateDate(new Date());
        role.setCreateUser(ShiroUtils.getCurrentUser().getId());
        if(!roleService.save(role))
            return R.failed(SimpleError.SAVE_ERROR);
        return R.ok(GlobalMessage.TIP_SAVE_SUCCESS);
    }


    /**
     * 修改角色
     * @param role
     * @return
     */
    @RequiresRoles("admin")
    @PostMapping("update_role")
    @ResponseBody
    @Transactional
    public R updateRole(Role role){
        if(role != null){
            if(role.getId() == 1){
                return R.failed(SimpleError.ADMIN_ERROR);
            }
            if(roleService.isExistRoleCodeOrName(role.getRoleCode(),role.getRoleName())){
                return R.failed(SimpleError.EXIST_NAME_ERROR);
            }
            role.setUpdateDate(new Date());
            role.setUpdateUser(ShiroUtils.getCurrentUser().getId());
            if(!roleService.updateById(role))
                return R.failed(SimpleError.OPERATION_ERROR);
        }
        return R.ok(GlobalMessage.TIP_OPERATION_SUCCESS);
    }



    /**
     * 查看角色列表
     * @return
     */
    @PostMapping("role_list")
    /*@RequiresPermissions("role:view")*/
    @ResponseBody
    public LayuiPage<Role> getRolePage(LayuiRequest layuiRequest){
        PageHelper.startPage(layuiRequest.getPage(),layuiRequest.getLimit());
        List<Role> list = roleService.list();
        PageInfo<Role> pageInfo = new PageInfo<>(list);
        return new LayuiPage<Role>(pageInfo.getTotal(),pageInfo.getList());
    }


    @PostMapping("get_role_list")
    @ResponseBody
    public List getRoleList(){
      return roleService.list();
    }


    /**
     * 获取全部权限
     * @return
     */
    @PostMapping("get_all_permission")
    @ResponseBody
    public List<TreeHolder> getAllPermission(){
        List<TreeHolder> main = new ArrayList<>();
        List<TreeHolder> user = new ArrayList<>();
        List<TreeHolder> dynamics = new ArrayList<>();
        List<TreeHolder> others = new ArrayList<>();

       List<Permission> permissions = permissionService.list();
       for(Permission p : permissions){
            if(p.getPermissionCode().indexOf("user") != -1){
                TreeHolder holder = new TreeHolder(p.getPermissionName(),p.getId());
                user.add(holder);
            }else if(p.getPermissionCode().indexOf("dynamics") != -1){
                TreeHolder holder = new TreeHolder(p.getPermissionName(),p.getId());
                dynamics.add(holder);
            }else{
                TreeHolder holder = new TreeHolder(p.getPermissionName(),p.getId());
                others.add(holder);
            }
       }
       TreeHolder userTree = new TreeHolder("用户管理",0l,true,user);
       TreeHolder dynamicsTree = new TreeHolder("动态管理",0l,true,dynamics);
       TreeHolder othersTree = new TreeHolder("其他权限",0l,true,others);
       main.add(userTree);
       main.add(dynamicsTree);
       main.add(othersTree);
       return main;
    }


    /**
     * 获取传入role id下的权限
     * @return
     */
   /* @RequiresPermissions("role:view")*/
    @PostMapping("get_permission_by_role_id")
    @ResponseBody
    public List getPermissionByRoleId(Long id){
        List<RolePermission> list = new ArrayList<>();
        list = rolePermissionService.list(new QueryWrapper<RolePermission>().lambda().eq(RolePermission::getRoleId,id));
        return list;
    }


    /**
     * 修改角色权限
     * @param rid 角色id
     * @param ids 权限id数组
     * @return R
     */
    @RequiresRoles("admin")
    @PostMapping("role_permission_update")
    @ResponseBody
    public R updatePermission(Integer rid, Integer[] ids){
        return rolePermissionService.updateRolePermission(rid,ids);
    }


    /**
     * 删除角色
     * @param id
     * @return
     */
    /*@RequiresPermissions("role:delete")*/
    @PostMapping("del_role")
    @ResponseBody
    @Transactional
    public R deleteRole(long id){
        if(id == 1)
            return R.failed(SimpleError.ADMIN_ERROR);
        if(!roleService.remove(new QueryWrapper<Role>().lambda().eq(Role::getId,id)))
            Assert.fail(SimpleError.DELETE_ERROR);
        if(!rolePermissionService.remove(new QueryWrapper<RolePermission>().lambda()
                .eq(RolePermission::getRoleId,id)))
            Assert.fail(SimpleError.DELETE_ERROR);
        return R.ok(GlobalMessage.TIP_OPERATION_SUCCESS);
    }


    /**
     * 废除管理员（删除用户拥有角色）超级管理员不能废除（ID为1的）
     * @param userId 用户ID
     * @return R
     */
    @PostMapping("cancel_role")
    @ResponseBody
    @Transactional
    public R cancelRole(Long userId){
        if(userId == null)
            return R.failed(SimpleError.NULL_POINT_ERROR);
        if(userId == 1)
            return R.failed(SimpleError.ADMIN_ERROR);
        if(!userRoleService.remove(new QueryWrapper<UserRole>().lambda().eq(UserRole::getUserId,userId)))
            return R.failed(SimpleError.OPERATION_ERROR);
        return R.ok(GlobalMessage.TIP_OPERATION_SUCCESS);
    }


    /**
     * 获取用户角色
     * @param userId 用户id
     * @return UserRole实体json
     */
    @PostMapping("get_user_role")
    @ResponseBody
    public UserRole getUserRole(Long userId){
        if(userId != null){
           UserRole userRole = userRoleService.getOne(new QueryWrapper<UserRole>().lambda()
                    .eq(UserRole::getUserId,userId));
           if(userRole != null){
               return userRole;
           }
        }
        return null;
    }


    /**
     * 赋予用户角色，超级管理员角色不能更改，赋予角色前先删除旧角色，再添加新角色
     * @param userRole 实体接收
     * @return R
     */
    @PostMapping("add_user_role")
    @ResponseBody
    @Transactional
    public R addUserRole(@Validated UserRole userRole){
        if(userRole.getUserId() == 1)
            return R.failed(SimpleError.ADMIN_ERROR);
        if(!userRoleService.remove(new QueryWrapper<UserRole>().lambda().eq(UserRole::getUserId,userRole.getUserId())))
            Assert.fail(SimpleError.OPERATION_ERROR);
        if(!userRoleService.save(userRole))
            Assert.fail(SimpleError.OPERATION_ERROR);
        return R.ok(GlobalMessage.TIP_OPERATION_SUCCESS);
    }



}
