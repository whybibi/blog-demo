package cn.why.blog.basic.service;

import cn.why.blog.basic.entity.Permission;
import cn.why.blog.basic.entity.RolePermission;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

public interface RolePermissionService extends IService<RolePermission> {

    List<RolePermission> getRolePermissionByRoleId(Integer roleId);

    Set<Permission> getPermissionByRoleId(Integer roleId);

    Set<String> getPermissionStringByRoleId(Integer roleId);

    R updateRolePermission(Integer rid, Integer[] ids);

}
