package cn.why.blog.config.mybatisplus;

import cn.why.blog.config.shiro.ShiroUtils;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @className: MetaObjectHandlerExtend
 * @description: MetaObjectHandler扩展，自定义填充公共字段 ,即没有传的字段自动填充
 **/
/*@Component*/
public class MetaObjectHandlerExtend implements MetaObjectHandler {


    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createDate", new Date(), metaObject);
        this.setFieldValByName("createUser", ShiroUtils.getCurrentUser() != null ? ShiroUtils.getCurrentUser().getId() : null , metaObject);
        this.setFieldValByName("updateDate", new Date(), metaObject);
        this.setFieldValByName("updateUser", ShiroUtils.getCurrentUser() != null ? ShiroUtils.getCurrentUser().getId() : null , metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateDate", new Date(), metaObject);
        this.setFieldValByName("updateUser", ShiroUtils.getCurrentUser() != null ? ShiroUtils.getCurrentUser().getId() : null , metaObject);
    }

}
