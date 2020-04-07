package cn.why.blog.basic.service;

import cn.why.blog.basic.entity.Permission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface PermissionService extends IService<Permission> {

    Permission getPermissionById(Integer id);

}
