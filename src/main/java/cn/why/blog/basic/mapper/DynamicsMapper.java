package cn.why.blog.basic.mapper;

import cn.why.blog.basic.entity.Dynamics;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface DynamicsMapper extends BaseMapper<Dynamics> {

    List<Dynamics> getDynamicsTopTen();

}
