package cn.why.blog.basic.entity.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

public enum IsSystemEnum implements IEnum<Integer>{
    ONE(1,"系统"),TWO(2,"普通");

    private Integer value;
    private String desc;

    IsSystemEnum(Integer value, String desc) {
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
