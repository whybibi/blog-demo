package cn.why.blog.basic.service;

import cn.why.blog.basic.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

public interface RoleService extends IService<Role>{

    Role getRoleById(Integer id);

    Boolean isExistRoleCodeOrName(String roleCode,String roleName);

}
