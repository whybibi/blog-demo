package cn.why.blog.basic.service.impl;

import cn.why.blog.basic.entity.Name;
import cn.why.blog.basic.mapper.NameMapper;
import cn.why.blog.basic.service.NameService;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class NameServiceImpl extends ServiceImpl<NameMapper,Name> implements NameService {
    @Override
    public R insertName(Name name) {
        if(baseMapper.insert(name) == 1)
            return R.ok("success");
        return R.failed("fail");
    }
}
