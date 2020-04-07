package cn.why.blog.basic.mapper;

import cn.why.blog.basic.entity.BasicUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface BasicUserMapper extends BaseMapper<BasicUser>{

    BasicUser getUserById(Long id);

}
