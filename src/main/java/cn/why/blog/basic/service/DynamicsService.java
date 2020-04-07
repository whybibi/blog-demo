package cn.why.blog.basic.service;

import cn.why.blog.basic.entity.Dynamics;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface DynamicsService extends IService<Dynamics>{

    /*提交个人新增动态*/
    R submitDynamics(HttpServletRequest request);

    /**
     * 获取前十条数据
     * @return
     */
    List<Dynamics> getDynamicsTopTen();

}
