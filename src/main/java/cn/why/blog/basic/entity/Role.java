package cn.why.blog.basic.entity;

import cn.why.blog.basic.entity.base.DataEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@TableName("ROLE")
public class Role extends DataEntity<Role>{

    @NotNull
    private String roleCode;
    @NotNull
    private String roleName;

    private Integer isSystem;

    private Integer isEnable;

    private String description;

}
