package cn.why.blog.basic.service.impl;

import cn.why.blog.basic.entity.Role;
import cn.why.blog.basic.entity.enums.IsEnableEnum;
import cn.why.blog.basic.mapper.RoleMapper;
import cn.why.blog.basic.service.RoleService;
import cn.why.blog.basic.service.UserRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper,Role> implements RoleService {

    /**
     * 通过角色id获取，如果角色为禁用状态返回null
     * @param id 角色id
     * @return role
     */
    @Override
    public Role getRoleById(Integer id) {
        Role role = baseMapper.selectById(id);
        if(role != null){
            if(role.getIsEnable() != IsEnableEnum.ONE.getValue())
                return null;
        }
        return role;
    }

    /**
     * 查询数据库中是否存在相同名称
     * @param roleCode 角色代码
     * @param roleName 角色名称
     * @return 存在返回true，不存在返回false
     */
    @Override
    public Boolean isExistRoleCodeOrName(String roleCode, String roleName) {
        if(roleCode != null){
           if(baseMapper.selectCount(new QueryWrapper<Role>().lambda().eq(Role::getRoleCode,roleCode)) > 0)
               return true;
        }
        if(roleName != null){
           if(baseMapper.selectCount(new QueryWrapper<Role>().lambda().eq(Role::getRoleName,roleName)) > 0)
               return true;
        }
        return false;
    }


}
