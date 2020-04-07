package cn.why.blog.basic.service;

import cn.why.blog.basic.entity.Role;
import cn.why.blog.basic.entity.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserRoleService extends IService<UserRole>{

    UserRole getUserRoleByUserId(Long userId);

    Role getRoleByUserId(Long userId);

}
