package cn.why.blog.basic.service.impl;

import cn.why.blog.basic.entity.Permission;
import cn.why.blog.basic.mapper.PermissionMapper;
import cn.why.blog.basic.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper,Permission> implements PermissionService {
    @Override
    public Permission getPermissionById(Integer id) {
        return baseMapper.selectById(id);
    }

}
