package cn.why.blog.basic.entity.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

public enum IsEnableEnum implements IEnum<Integer>{
    ZERO(0,"冻结"),ONE(1,"启用");

    private Integer value;
    private String desc;

    IsEnableEnum(Integer value, String desc) {
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
