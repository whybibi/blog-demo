package cn.why.blog.core.service;

import cn.why.blog.basic.entity.BasicUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @version: V1.0
 * @auther: whybibi
 * @className:
 * @date: 2019-06-06 15:29
 * @description: 用户管理服务接口
 *
 */
public interface UserManagerService extends IService<BasicUser>{

    /**
     * 通过用户ID更新
     * @param user
     * @return boolean
     */
    Boolean updateUserById(BasicUser user);

    Boolean recoverPassWord(Long id);

}
