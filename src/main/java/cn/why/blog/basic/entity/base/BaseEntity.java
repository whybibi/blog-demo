package cn.why.blog.basic.entity.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

@Data
public class BaseEntity<T extends Model> extends Model<T> {

    @TableId(type = IdType.AUTO)
    protected Long id;

    public BaseEntity() {
    }
    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
