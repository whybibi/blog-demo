package cn.why.blog.basic.service;

import cn.why.blog.basic.entity.Name;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.service.IService;

public interface NameService extends IService<Name> {

    R insertName(Name name);

}
