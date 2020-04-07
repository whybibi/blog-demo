package cn.why.blog.basic.service.impl;

import cn.why.blog.basic.entity.Role;
import cn.why.blog.basic.entity.UserRole;
import cn.why.blog.basic.mapper.UserRoleMapper;
import cn.why.blog.basic.service.RoleService;
import cn.why.blog.basic.service.UserRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper,UserRole> implements UserRoleService {

    @Autowired
    private RoleService roleService;

    /**
     * 根据用户id获取对应的用户角色表对象
     * @param userId
     * @return
     */
    @Override
    public UserRole getUserRoleByUserId(Long userId) {
        return baseMapper.selectOne(new QueryWrapper<UserRole>().lambda().eq(UserRole::getUserId,userId));
    }

    @Override
    public Role getRoleByUserId(Long userId) {
        return roleService.getRoleById(this.getUserRoleByUserId(userId).getRoleId());
    }
}
