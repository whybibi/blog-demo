package cn.why.blog.basic.service;

import cn.why.blog.basic.entity.BasicUser;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

public interface BasicUserService extends IService<BasicUser>{

    BasicUser getBasicUserById(Long id);

    BasicUser getBasicUserByName(String name);

    R  registerUser(HttpServletRequest request);

    R deleteUserById(Long id);

}
