package cn.why.blog.basic.entity;

import cn.why.blog.basic.entity.base.DataEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@TableName("comment")
public class Comment  extends DataEntity<Comment>{

    @NotNull
    private Long dynamicsId;

    private Long userId;

    @TableField(exist = false)
    private BasicUser user;

    @TableField(exist = false)
    private Dynamics dynamics;

    @NotNull
    private String commentMsg;

    private Integer isDelete;

    private Integer isEnable;

}
