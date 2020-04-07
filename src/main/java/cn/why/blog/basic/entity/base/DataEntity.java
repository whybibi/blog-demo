package cn.why.blog.basic.entity.base;

import cn.why.blog.basic.entity.BasicUser;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class DataEntity<T extends Model> extends BaseEntity<T> {

    @DateTimeFormat
    protected Date createDate;

    @TableField(fill = FieldFill.INSERT)
    protected Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @DateTimeFormat
    protected Date updateDate;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected Long updateUser;

    @TableField(exist = false)
    protected BasicUser createBasicUser;

    @TableField(exist = false)
    protected BasicUser updateBasicUser;


}
