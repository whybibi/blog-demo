package cn.why.blog.core.service.impl;

import cn.why.blog.basic.entity.BasicUser;
import cn.why.blog.basic.entity.base.GlobalMessage;
import cn.why.blog.basic.entity.base.GlobalPasswordVariable;
import cn.why.blog.basic.mapper.BasicUserMapper;
import cn.why.blog.config.shiro.ShiroUtils;
import cn.why.blog.core.service.UserManagerService;
import cn.why.blog.utils.PassWordUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @version: V1.0
 * @auther: whybibi
 * @className: UserManagerServiceImpl
 * @date: 2019-06-06 15:30
 * @description: 用户接口实现
 */
@Service
public class UserManagerServiceImpl extends ServiceImpl<BasicUserMapper,BasicUser> implements UserManagerService{


    /**
     * 更新用户
     * @param user
     * @return
     */
    @Override
    @Transactional
    public Boolean updateUserById(BasicUser user) {
        if(user != null){
            user.setUpdateDate(new Date());
            user.setUpdateUser(ShiroUtils.getCurrentUser().getId());
            if(super.updateById(user))
                return true;
        }
        return false;
    }

    /**
     * 重置密码为123456
     * @param id 用户id
     * @return boolean
     */
    @Override
    @Transactional
    public Boolean recoverPassWord(Long id) {
        BasicUser user = super.getById(id);
        if(user != null){
            String salt = PassWordUtils.generateSalt(GlobalPasswordVariable.SALT_NUM);
            String password = PassWordUtils.encryptPassword(GlobalPasswordVariable.ENCRYPTION_TYPE, "123456", salt, GlobalPasswordVariable.ENCRYPTION_NUM);
            user.setSalt(salt);
            user.setPassWord(password);
            if(super.updateById(user)){
                return true;
            }
        }
        return false;
    }


}
