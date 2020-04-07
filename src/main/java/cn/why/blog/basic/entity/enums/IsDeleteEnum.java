package cn.why.blog.basic.entity.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

public enum IsDeleteEnum implements IEnum<Integer>{
    ZERO(0,"删除"),ONE(1,"正常");

    private Integer value;
    private String desc;

    IsDeleteEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
