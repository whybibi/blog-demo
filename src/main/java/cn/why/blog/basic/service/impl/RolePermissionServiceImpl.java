package cn.why.blog.basic.service.impl;

import cn.why.blog.basic.entity.Permission;
import cn.why.blog.basic.entity.Role;
import cn.why.blog.basic.entity.RolePermission;
import cn.why.blog.basic.entity.base.GlobalMessage;
import cn.why.blog.basic.entity.enums.SimpleError;
import cn.why.blog.basic.mapper.RolePermissionMapper;
import cn.why.blog.basic.service.PermissionService;
import cn.why.blog.basic.service.RolePermissionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.Assert;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper,RolePermission> implements RolePermissionService{

    @Autowired
    private PermissionService permissionService;

    /**
     * 根据角色id查询相应的权限
     * @param roleId
     * @return
     */
    @Override
    public List<RolePermission> getRolePermissionByRoleId(Integer roleId) {
        return baseMapper.selectList(new QueryWrapper<RolePermission>().lambda().eq(RolePermission::getRoleId,roleId));
       }

    /**
     * 获取角色下的权限
     * @param roleId 角色id
     * @return Set<Permission>
     */
    @Override
    public Set<Permission> getPermissionByRoleId(Integer roleId) {
        List<RolePermission> rpList = this.getRolePermissionByRoleId(roleId);
        Set<Permission> pList = new HashSet<>();
        for(RolePermission rp : rpList){
            pList.add(permissionService.getPermissionById(rp.getPermissionId()));
        }
        return pList;
    }

    /**
     * 通过角色id获取权限名
     * @param roleId 角色id
     * @return Set<String>
     */
    @Override
    public Set<String> getPermissionStringByRoleId(Integer roleId) {
        List<RolePermission> rpList = this.getRolePermissionByRoleId(roleId);
        Set<String> pList = new HashSet<>();
        for(RolePermission rp : rpList){
            pList.add(permissionService.getPermissionById(rp.getPermissionId()).getPermissionCode());
        }
        return pList;
    }

    /**
     * 更新角色拥有权限
     * @param rid 角色id
     * @param ids 权限id数组
     * @return R
     */
    @Override
    @Transactional
    public R updateRolePermission(Integer rid, Integer[] ids) {
        if(rid == null)
            return R.failed(SimpleError.NULL_POINT_ERROR);
        if(rid == 1)
            return R.failed(SimpleError.ADMIN_ERROR);
        if(ids == null || ids.length == 0){
            if(baseMapper.selectList(new QueryWrapper<RolePermission>().lambda().eq(RolePermission::getRoleId,rid)).size() > 0){
                if(super.remove(new QueryWrapper<RolePermission>().lambda().eq(RolePermission::getRoleId,rid)))
                    Assert.fail(SimpleError.OPERATION_ERROR);
            }else{
                return R.ok(GlobalMessage.TIP_OPERATION_SUCCESS);
            }
        }else{
            List<RolePermission> rpList = baseMapper.selectList(new QueryWrapper<RolePermission>().lambda().eq(RolePermission::getRoleId,rid));
            List<Integer> pids = new ArrayList<>();//当前角色在数据库中存在的权限
            List<Integer> dels = new ArrayList<>();//要删除的权限id（初始化时放入全部）
            for(RolePermission rp : rpList){
                pids.add(rp.getPermissionId());
                dels.add(rp.getPermissionId());
            }
            /*循环找出要删除的权限id（数据库中存在但传入数组中不存在的id）*/
            for(int i=0; i < ids.length; i++){
                for(int j=0; j < dels.size(); j++){//如果传入的某个id和数据库中的相同，就从删除集合中去除
                    if(ids[i] == dels.get(j)){
                        dels.remove(j);
                    }
                }
            }
            /*循环找出要新增的id（数据库中不存在但传入数组存在的id）*/
            List<Integer> adds = new ArrayList<>();
            for(int i=0; i<ids.length; i++){
                if(!pids.contains(ids[i])){//如果数据库中不包含某个传入id，就把此id放入新增集合中
                    adds.add(ids[i]);
                }
            }

            System.out.println("要删除的id:"+dels);
            System.out.println("要新增的id:"+adds);

            //循环删除权限
            if(dels.size() > 0){
                for (int i=0; i<dels.size(); i++){
                    if(baseMapper.delete(new QueryWrapper<RolePermission>().lambda()
                            .eq(RolePermission::getRoleId,rid)
                            .eq(RolePermission::getPermissionId,dels.get(i))) != 1)
                        Assert.fail(SimpleError.DELETE_ERROR);
                }
            }

            //循环新增权限
            if(adds.size() > 0){
                for(int i=0; i < adds.size(); i++){
                    RolePermission rolePermission = new RolePermission(rid,adds.get(i),1);
                    if(baseMapper.insert(rolePermission) != 1)
                        Assert.fail(SimpleError.SAVE_ERROR);
                }
            }
        }
        return R.ok(GlobalMessage.TIP_OPERATION_SUCCESS);
    }
}
